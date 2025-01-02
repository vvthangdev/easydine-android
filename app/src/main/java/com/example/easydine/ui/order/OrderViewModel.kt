package com.example.easydine.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.Order
import com.example.easydine.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val allOrders: LiveData<List<Order>> = orderRepository.allOrders

    // Fetch đơn hàng từ API và lưu vào DB
    fun fetchAndSaveOrders() {
        viewModelScope.launch {
            orderRepository.fetchAndSaveOrders()
        }
    }

    suspend fun getOrderById(orderId: Int): Order? {
        return orderRepository.getOrderById(orderId)
    }
}
