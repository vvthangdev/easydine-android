package com.example.easydine.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.Food
import com.example.easydine.data.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {

    // LiveData cho danh sách món ăn
    val foods: LiveData<List<Food>> = repository.allFoods

    // LiveData cho danh sách món ăn trong giỏ hàng
    val cartItems: LiveData<List<Food>> = repository.getCartItems()

    // LiveData cho số lượng món trong giỏ hàng
    val cartItemCount: LiveData<Int> = repository.cartItemCount

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    // Hàm tính tổng tiền
    fun calculateTotalPrice(cartItems: List<Food>) {
        val total = cartItems.sumOf { it.price * it.quantity }
        _totalPrice.postValue(total)
    }

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
    fun addToCart(foodId: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToCart(foodId, quantity)
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

    fun increaseQuantity(foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentFood = repository.getFoodById(foodId)
            if (currentFood != null) {
                repository.updateQuantity(foodId, currentFood.quantity + 1)
            }
        }
    }

    fun decreaseQuantity(foodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentFood = repository.getFoodById(foodId)
            if (currentFood != null && currentFood.quantity > 0) {
                repository.updateQuantity(foodId, currentFood.quantity - 1)
            }
        }
    }

}
