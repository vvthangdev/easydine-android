package com.example.easydine.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easydine.data.model.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods")
    fun getAllFoods(): LiveData<List<Food>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)

    @Query("SELECT COUNT(*) FROM foods WHERE inCart = 1")
    fun getCartItemCount(): LiveData<Int>

    @Query("UPDATE foods SET inCart = :inCart WHERE id = :foodId")
    suspend fun updateCartStatus(foodId: Int, inCart: Boolean)
}