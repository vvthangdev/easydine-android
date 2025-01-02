package com.example.easydine.data.network.service

import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.data.network.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApiService {
    @POST("orders/create-order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<OrderResponse>
}
