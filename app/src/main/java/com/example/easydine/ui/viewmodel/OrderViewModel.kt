package com.example.easydine.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.data.network.response.OrderResponse
import com.example.easydine.data.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orderResult = MutableLiveData<Result<OrderResponse>?>()
    val orderResult: LiveData<Result<OrderResponse>?> get() = _orderResult
//    var lastSubmitTime: Long = 0

    fun createOrder(orderRequest: OrderRequest) {
        viewModelScope.launch {
            val result = orderRepository.createOrder(orderRequest)
            _orderResult.postValue(result)
        }
    }

    fun clearOrderResult() {
        _orderResult.value = null
    }

}
