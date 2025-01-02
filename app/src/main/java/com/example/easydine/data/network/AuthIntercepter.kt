package com.example.easydine.data.network

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Lấy Access Token từ SharedPreferences
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        // Log giá trị Access Token để kiểm tra
        Log.d("AuthInterceptor", "Access Token: $accessToken")

        // Nếu có Access Token, thêm vào header Authorization
        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "$accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}

