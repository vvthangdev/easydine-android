package com.example.easydine.di

import android.app.Application
import androidx.room.Room
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.local.dao.ImageBannerDao
import com.example.easydine.data.local.database.AppDatabase
import com.example.easydine.data.network.AuthInterceptor
import com.example.easydine.data.network.service.FoodApiService
import com.example.easydine.data.network.service.ImageBannerService
import com.example.easydine.data.network.service.OrderApiService
import com.example.easydine.data.network.service.UserApiService
import com.example.easydine.data.repositories.FoodRepository
import com.example.easydine.data.repositories.UserRepository
import com.example.easydine.utils.Constants
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

    private const val BASE_URL = Constants.BASE_URL

    // --- Database and DAO ---
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "food_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFoodDao(database: AppDatabase): FoodDao = database.foodDao()

    @Provides
    fun provideImageBannerDao(database: AppDatabase): ImageBannerDao = database.imageBannerDao()

    // --- Moshi ---
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // --- Logging Interceptor ---
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // --- AuthInterceptor ---
    @Provides
    @Singleton
    fun provideAuthInterceptor(app: Application): AuthInterceptor {
        return AuthInterceptor(app)
    }

    // --- OkHttpClient ---
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        app: Application
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(app)) // Thêm AuthInterceptor
            .build()
    }

    // --- Retrofit ---
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    // --- API Services ---
    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFoodApiService(retrofit: Retrofit): FoodApiService {
        return retrofit.create(FoodApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageBannerService(retrofit: Retrofit): ImageBannerService {
        return retrofit.create(ImageBannerService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
        return retrofit.create(OrderApiService::class.java)
    }

    // --- Repositories ---
    @Provides
    fun provideUserRepository(apiService: UserApiService): UserRepository {
        return UserRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodDao,
        foodApiService: FoodApiService
    ): FoodRepository {
        return FoodRepository(foodDao, foodApiService)
    }
}
