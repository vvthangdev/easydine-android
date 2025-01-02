package com.example.easydine.data.repositories

import androidx.lifecycle.LiveData
import com.example.easydine.data.local.dao.OrderDao
import com.example.easydine.data.model.Order
import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.data.network.response.OrderResponse
import com.example.easydine.data.network.response.toOrder
import com.example.easydine.data.network.service.OrderApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
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

    val allOrders: LiveData<List<Order>> = orderDao.getAllOrders()

    suspend fun getOrderById(orderId: Int): Order? {
        return orderDao.getOrderById(orderId)
    }

    suspend fun fetchAndSaveOrders() {
        try {
            // Gọi API và xử lý phản hồi
            val response = orderApiService.getAllOrders()
            if (response.isSuccessful) {
                response.body()?.let { orderResponses ->
                    // Ánh xạ từ OrderResponse sang Order
                    val orders = orderResponses.map { it.toOrder() }
                    // Lưu danh sách đơn hàng vào cơ sở dữ liệu
                    orderDao.insertOrders(orders)
                }
            }
        } catch (e: IOException) {
            // Xử lý lỗi kết nối
            e.printStackTrace()
        } catch (e: HttpException) {
            // Xử lý lỗi từ server
            e.printStackTrace()
        }
    }

    // Cập nhật trạng thái đơn hàng
    suspend fun updateOrder(order: Order) {
        try {
            orderDao.updateOrder(order)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Xóa đơn hàng
    suspend fun deleteOrder(order: Order) {
        try {
            orderDao.deleteOrder(order)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

