package com.example.easydine.data.network.response

import com.example.easydine.data.model.Food
import com.squareup.moshi.Json

data class FoodResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Double,
    @Json(name = "image") val image: String
)

//public fun FoodResponse.toFood(): Food {
//    return Food(
//        id = this.id,
//        name = this.name,
//        price = this.price,
//        image = this.image
//    )
//}
