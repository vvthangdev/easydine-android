package com.example.easydine.data.network.response

import com.example.easydine.data.model.Food

fun FoodResponse.toFood(): Food {
    return Food(
        id = this.id,
        name = this.name,
        price = this.price,
        image = this.image

    )
}
