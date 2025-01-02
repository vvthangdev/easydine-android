package com.example.easydine.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.Food
import com.example.easydine.data.repositories.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    val cartItems: LiveData<List<Food>> = foodRepository.getCartItems()

    suspend fun getCartItemsSync(): List<Food> {
        return foodRepository.getCartItemsSync()
    }


    fun fetchAndSaveFoods() {
        viewModelScope.launch {
            foodRepository.fetchAndSaveFoods()
        }
    }
}
