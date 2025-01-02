package com.example.easydine.data.network

import android.content.Context
import com.example.easydine.data.network.service.UserApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor(
    private val context: Context,
    private val userApiService: UserApiService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            // Lấy Refresh Token từ SharedPreferences
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val refreshToken = sharedPreferences.getString("refreshToken", null)

            if (!refreshToken.isNullOrEmpty()) {
                // Làm mới Access Token đồng bộ
                val newAccessToken = runBlocking {
                    val refreshResponse = userApiService.refreshAccessToken("Bearer $refreshToken")
                    if (refreshResponse.isSuccessful) {
                        refreshResponse.body()?.accessToken
                    } else null
                }

                if (!newAccessToken.isNullOrEmpty()) {
                    // Lưu Access Token mới
                    sharedPreferences.edit()
                        .putString("accessToken", newAccessToken)
                        .apply()

                    // Tạo lại request với Access Token mới
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }
}
