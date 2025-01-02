package com.example.easydine.data.network.response

import com.squareup.moshi.Json
import com.example.easydine.data.model.Order

// Data class ánh xạ từ API response
data class OrderResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "customer_id") val customerId: Int,
    @Json(name = "time") val time: String,
    @Json(name = "num_people") val numPeople: String,
    @Json(name = "type") val type: String,
    @Json(name = "status") val status: String
)

// Extension function chuyển đổi từ OrderResponse sang Order (để lưu vào database)
fun OrderResponse.toOrder(): Order {
    return Order(
        id = this.id,
        customerId = this.customerId,
        time = this.time,
        numPeople = this.numPeople.toInt(), // Đảm bảo chuyển numPeople về kiểu Int
        type = this.type,
        status = this.status,
        star = null,  // Nếu không có trường này trong response, giữ null
        comment = null // Cũng tương tự với comment
    )
}
