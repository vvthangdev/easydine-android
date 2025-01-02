package com.example.easydine.data.repositories

import androidx.lifecycle.LiveData
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.model.Food
import com.example.easydine.data.network.response.FoodResponse
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
            // Gọi API và xử lý phản hồi
            val response = foodApiService.getFoods()
            if (response.isSuccessful) {
                response.body()?.let { foodResponses ->
                    // Ánh xạ từ FoodResponse sang Food
                    val newFoods = foodResponses.map { it.toFood() }

                    // Lấy danh sách hiện có từ cơ sở dữ liệu
                    val currentFoods = foodDao.getAllFoods()

                    // Merge dữ liệu mới từ API với dữ liệu hiện có
                    val mergedFoods = newFoods.map { newFood ->
                        val existingFood = foodDao.getFoodById(newFood.id)
                        if (existingFood != null) {
                            // Giữ lại giá trị quantity từ cơ sở dữ liệu
                            newFood.copy(quantity = existingFood.quantity)
                        } else {
                            // Nếu là món mới, giữ giá trị mặc định
                            newFood
                        }
                    }

                    // Lưu danh sách món ăn đã merge vào Room database
                    foodDao.insertFoods(mergedFoods)
                }
            } else {
                // Xử lý lỗi từ server (4xx hoặc 5xx)
                throw HttpException(response)
            }
        } catch (e: IOException) {
            // Xử lý lỗi khi không thể kết nối đến server (mất mạng, DNS, etc.)
            e.printStackTrace()
        } catch (e: HttpException) {
            // Xử lý lỗi từ server
            e.printStackTrace()
        }
    }


}