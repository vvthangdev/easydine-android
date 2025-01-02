package com.example.easydine.data.repositories

import androidx.lifecycle.LiveData
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.model.Food
import com.example.easydine.data.network.response.toFood
import com.example.easydine.data.network.service.FoodApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val foodApiService: FoodApiService
) {
    val allFoods: LiveData<List<Food>> = foodDao.getAllFoods()
    val cartItemCount: LiveData<Int> = foodDao.getCartItemCount()

    // Lấy danh sách các món ăn trong giỏ hàng
    fun getCartItems(): LiveData<List<Food>> {
        return foodDao.getCartItems()
    }

    suspend fun getCartItemsSync(): List<Food> {
        return foodDao.getCartItemsSync() // Hàm này cần thêm trong DAO
    }

    /**
     * Add item to cart.
     */
    suspend fun addToCart(foodId: Int, quantity: Int) {
        val currentFood = foodDao.getFoodById(foodId)
        if (currentFood != null) {
            val newQuantity = currentFood.quantity + quantity
            foodDao.updateQuantity(foodId, newQuantity)
        } else {
            // Thêm món ăn mới nếu chưa có trong cơ sở dữ liệu
            val food = Food(foodId, "Unknown", 0.0, "", quantity)
            foodDao.insertFoods(listOf(food))
        }
    }

    /**
     * Remove item from cart.
     */
    suspend fun removeFromCart(foodId: Int) {
        foodDao.updateQuantity(foodId, 0)
    }

    suspend fun updateQuantity(foodId: Int, quantity: Int) {
        val currentFood = foodDao.getFoodById(foodId)
        if (currentFood != null) {
            foodDao.updateQuantity(foodId, quantity)
        }
    }

    suspend fun getFoodById(foodId: Int): Food? {
        return foodDao.getFoodById(foodId)
    }

    /**
     * Fetch data from the API and save to the database.
     */
    suspend fun fetchAndSaveFoods() {
        try {
            val response = foodApiService.getFoods()
            if (response.isSuccessful) {
                response.body()?.let { foodResponses ->
                    // Ánh xạ từ FoodResponse sang Food
                    val newFoods = foodResponses.map { it.toFood() }

                    // Lấy danh sách hiện có từ cơ sở dữ liệu
                    val currentFoods = foodDao.getAllFoodsSync() // Thêm phương thức này vào DAO

                    // Tạo một Set các ID từ API để kiểm tra món ăn đã bị xóa
                    val newFoodIds = newFoods.map { it.id }.toSet()

                    // Tìm các món ăn đã bị xóa
                    val deletedFoodIds = currentFoods
                        .filter { currentFood -> !newFoodIds.contains(currentFood.id) }
                        .map { it.id }

                    // Kiểm tra xem có thay đổi nào không
                    val hasChanges = deletedFoodIds.isNotEmpty() ||
                            newFoods.any { newFood ->
                                val existingFood = currentFoods.find { it.id == newFood.id }
                                existingFood == null ||
                                        existingFood.name != newFood.name ||
                                        existingFood.price != newFood.price ||
                                        existingFood.image != newFood.image
                            }

                    if (hasChanges) {
                        // Xóa các món ăn không còn tồn tại
                        if (deletedFoodIds.isNotEmpty()) {
                            foodDao.deleteFoodsByIds(deletedFoodIds)
                        }

                        // Merge dữ liệu mới với dữ liệu hiện có
                        val mergedFoods = newFoods.map { newFood ->
                            val existingFood = currentFoods.find { it.id == newFood.id }
                            if (existingFood != null) {
                                newFood.copy(quantity = existingFood.quantity)
                            } else {
                                newFood
                            }
                        }

                        // Chỉ cập nhật database khi có thay đổi
                        foodDao.insertFoods(mergedFoods)
                    }
                }
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}
