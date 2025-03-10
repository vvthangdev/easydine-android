package com.example.easydine.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.easydine.data.model.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods")
    fun getAllFoods(): LiveData<List<Food>>

    @Query("SELECT * FROM foods WHERE id = :foodId LIMIT 1")
    suspend fun getFoodById(foodId: Int): Food?

    @Query("SELECT COUNT(*) FROM foods WHERE quantity > 0")
    fun getCartItemCount(): LiveData<Int>

    @Query("SELECT * FROM foods WHERE quantity > 0")
    suspend fun getCartItemsSync(): List<Food>

    @Query("SELECT * FROM foods WHERE quantity >= 1")
    fun getCartItems(): LiveData<List<Food>>

    @Query("UPDATE foods SET quantity = :quantity WHERE id = :foodId")
    suspend fun updateQuantity(foodId: Int, quantity: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)

    @Query("DELETE FROM foods WHERE id IN (:foodIds)")
    suspend fun deleteFoodsByIds(foodIds: List<Int>)

    @Query("SELECT * FROM foods")
    suspend fun getAllFoodsSync(): List<Food>
}