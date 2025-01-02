package com.example.easydine.data.repositories

import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.data.network.response.OrderResponse
import com.example.easydine.data.network.service.OrderApiService
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderApiService: OrderApiService
) {
    suspend fun createOrder(orderRequest: OrderRequest): Result<OrderResponse> {
        return try {
            val response = orderApiService.createOrder(orderRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

