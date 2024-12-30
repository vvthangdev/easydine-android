package com.example.easydine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey val id: Int,
    val name: String,
//    val restaurant: String, // Trường bổ sung (có thể thêm thủ công)
    val price: Double,
    val image: String,
    var inCart: Boolean = false // Trạng thái món ăn trong giỏ hàng
)