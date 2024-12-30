package com.example.easydine.di

import android.app.Application
import androidx.room.Room
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.local.dao.ImageBannerDao
import com.example.easydine.data.local.database.AppDatabase
import com.example.easydine.data.network.service.FoodApiService
import com.example.easydine.data.network.service.ImageBannerService
import com.example.easydine.data.network.service.UserApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://f9c2-2a09-bac1-7a80-50-00-245-e6.ngrok-free.app/"

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        // Xóa toàn bộ cơ sở dữ liệu cũ
//        app.deleteDatabase("food_database") // Tên cơ sở dữ liệu là "food_database"

        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "food_database" // Tên database
        )
            .fallbackToDestructiveMigration() // Xử lý migration tự động
            .build()
    }

    // Cung cấp Moshi instance
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // Cung cấp HttpLoggingInterceptor
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Cung cấp OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Cung cấp Retrofit instance
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // URL duy nhất
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    // Cung cấp FoodApiService
    @Provides
    @Singleton
    fun provideFoodApiService(retrofit: Retrofit): FoodApiService {
        return retrofit.create(FoodApiService::class.java)
    }

    @Provides
    fun provideFoodDao(database: AppDatabase): FoodDao {
        return database.foodDao()
    }

    // 3. Cung cấp ImageBannerDao
    @Provides
    fun provideImageBannerDao(database: AppDatabase): ImageBannerDao {
        return database.imageBannerDao()
    }


    // Cung cấp ImageBannerService
    @Provides
    @Singleton
    fun provideImageBannerService(retrofit: Retrofit): ImageBannerService {
        return retrofit.create(ImageBannerService::class.java)
    }

    // Cung cấp UserApiService
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
}
