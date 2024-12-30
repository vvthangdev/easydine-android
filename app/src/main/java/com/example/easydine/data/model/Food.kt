package com.example.easydine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val image: String,
//    var inCart: Boolean = false, // Trạng thái món ăn trong giỏ hàng
    var quantity: Int = 0 // Số lượng mặc định là 0 (không có trong giỏ hàng)
)
