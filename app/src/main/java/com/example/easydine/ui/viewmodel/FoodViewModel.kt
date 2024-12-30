package com.example.easydine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.Food
import com.example.easydine.data.repositories.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {

    // LiveData cho danh sách món ăn
    val foods: LiveData<List<Food>> = repository.allFoods

    // LiveData cho số lượng món trong giỏ hàng
    val cartItemCount: LiveData<Int> = repository.cartItemCount

    /**
     * Gọi API để lấy danh sách món ăn và lưu vào Room database.
     */
    fun fetchAndSaveFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchAndSaveFoods()
        }
    }

    /**
     * Thêm món ăn vào giỏ hàng.
     */
    fun addToCart(foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToCart(foodId)
        }
    }

    /**
     * Xóa món ăn khỏi giỏ hàng.
     */
    fun removeFromCart(foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromCart(foodId)
        }
    }
}