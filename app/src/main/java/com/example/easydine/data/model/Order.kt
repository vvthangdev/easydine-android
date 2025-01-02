package com.example.easydine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: Int,  // ID sẽ do API gửi về
    val customerId: Int,
    val time: String,
    val type: String,
    val numPeople: Int,
    val status: String,
    val star: Int?,
    val comment: String?
)

