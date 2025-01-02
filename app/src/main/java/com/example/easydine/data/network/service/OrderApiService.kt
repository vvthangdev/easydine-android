package com.example.easydine.data.network.service

import com.example.easydine.data.model.Order
import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.data.network.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApiService {
    @POST("orders/create-order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<OrderResponse>

    @GET("a")
    suspend fun getAllUserOrders(): Response<List<Order>>
//
//    @GET("orders/{id}")
//    suspend fun getOrderById(@Path("id") orderId: Int): Response<OrderResponse>

    @GET("orders/get-all-user-orders")
    suspend fun getAllOrders(): Response<List<OrderResponse>>
}
