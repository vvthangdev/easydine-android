package com.example.easydine.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.local.dao.ImageBannerDao
import com.example.easydine.data.model.Food
import com.example.easydine.data.model.ImageBanner

@Database(entities = [Food::class, ImageBanner::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun imageBannerDao(): ImageBannerDao
}
