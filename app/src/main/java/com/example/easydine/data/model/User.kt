package com.example.easydine.data.model

data class User(
    val id: Int ?= -1,
    var role: String?,
    var name: String?,
    var status: String?,
    var message: String?,
    var address: String?,
    var avatar: String?,
    var bio: String?,
    var email: String?,
    var phone: String?,
    var username: String?,
    var accessToken: String?,
    var refreshToken: String?
)