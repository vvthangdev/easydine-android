package com.example.easydine.data.network.response

import com.squareup.moshi.Json

data class OrderResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "customer_id") val customerId: Int,
    @Json(name = "time") val time: String,
    @Json(name = "num_people") val numPeople: String,
    @Json(name = "type") val type: String,
    @Json(name = "status") val status: String
)
