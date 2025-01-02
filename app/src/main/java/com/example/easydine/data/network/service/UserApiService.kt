package com.example.easydine.data.network.service

import com.example.easydine.data.model.User
import com.example.easydine.data.model.UserUpdateRequest
import com.example.easydine.data.network.response.LoginResponse
import com.example.easydine.data.network.response.RefreshTokenResponse
import com.example.easydine.data.network.response.SignUpResponse
import com.example.easydine.data.network.response.UserUpdateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {
    @FormUrlEncoded
    @POST("/api/auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("/api/auth/refresh-token")
    suspend fun refreshAccessToken(
        @Header("Authorization") authorization: String
    ): Response<RefreshTokenResponse>

    @FormUrlEncoded
    @POST("/api/auth/signup")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
    ): Response<SignUpResponse>

    @GET("/api/auth/user-info")
    suspend fun getUserDataApi(
    ): Response<User>

    @PATCH("/api/auth/update-user")
    suspend fun updateUserDataApi(
        @Body userUpdateRequest: UserUpdateRequest
    ): Response<UserUpdateResponse>
}
