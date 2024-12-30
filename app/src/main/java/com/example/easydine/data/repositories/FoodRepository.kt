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
                    val foods = foodResponses.map { it.toFood() }
                    // Lưu danh sách món ăn vào Room database
                    foodDao.insertFoods(foods)
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

    /**
     * Add item to cart.
     */
    suspend fun addToCart(foodId: Int) {
        foodDao.updateCartStatus(foodId, true)
    }

    /**
     * Remove item from cart.
     */
    suspend fun removeFromCart(foodId: Int) {
        foodDao.updateCartStatus(foodId, false)
    }
}