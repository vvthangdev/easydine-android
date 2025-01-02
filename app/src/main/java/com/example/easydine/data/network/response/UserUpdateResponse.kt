package com.example.easydine.data.network.response

import com.squareup.moshi.Json

data class UserUpdateResponse(
    @Json(name = "status") val status: String,
    @Json(name = "message") val message: String,
    @Json(name = "user") val user: user?
)

data class user(
    @Json(name = "id") val id: Int,
    @Json(name = "role") val role: String,
    @Json(name = "name") val name: String?,
    @Json(name = "address") val address: String?,
    @Json(name = "avatar") val avatar: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "username") val username: String?,
    @Json(name = "phone") val phone: String?,
//    @Json(name = "password") val password: String?,
//    @Json(name = "refresh_token") val refreshToken: String?
)
