package com.example.easydine

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.easydine.data.local.dao.FoodDao
import com.example.easydine.data.local.dao.ImageBannerDao
import com.example.easydine.data.local.dao.OrderDao
import com.example.easydine.data.local.database.AppDatabase
import com.example.easydine.data.network.AuthInterceptor
import com.example.easydine.data.network.service.FoodApiService
import com.example.easydine.data.network.service.ImageBannerService
import com.example.easydine.data.network.service.OrderApiService
import com.example.easydine.data.network.service.UserApiService
import com.example.easydine.data.repositories.FoodRepository
import com.example.easydine.data.repositories.OrderRepository
import com.example.easydine.data.repositories.UserRepository
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
//        app.deleteDatabase("food_database")
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
    fun provideOrderDao(database: AppDatabase): OrderDao = database.orderDao()

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

//    @Provides
//    @Singleton
//    fun provideOrderApiService(retrofit: Retrofit): OrderApiService {
//        return retrofit.create(OrderApiService::class.java)
//    }

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

    // Cung cấp Context
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    // Cung cấp SharedPreferences mà không cần sử dụng Context trong các constructor khác
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }

    // --- Repositories ---
    // Cung cấp UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(
        sharedPreferences: SharedPreferences,
        apiService: UserApiService
    ): UserRepository {
        return UserRepository(sharedPreferences, apiService)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodDao,
        foodApiService: FoodApiService
    ): FoodRepository {
        return FoodRepository(foodDao, foodApiService)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        orderDao: OrderDao,
        orderApiService: OrderApiService
    ): OrderRepository {
        return OrderRepository(orderDao, orderApiService)
    }

}
