package com.example.easydine.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.easydine.data.model.Order

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<Order>)

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Int): Order?

    @Query("SELECT * FROM orders")
    fun getAllOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status")
    fun getOrdersByStatus(status: String): LiveData<List<Order>>
}
