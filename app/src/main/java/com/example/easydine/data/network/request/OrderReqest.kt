package com.example.easydine.data.network.request

data class OrderRequest(
    val type: String,
    val status: String,
    val start_time: String,
    val num_people: Int,
    val foods: List<FoodItem>? = null
)

data class FoodItem(
    val id: Int,
    val quantity: Int
)
