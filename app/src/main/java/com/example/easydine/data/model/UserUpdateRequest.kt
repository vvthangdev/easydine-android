package com.example.easydine.data.model

data class UserUpdateRequest(
    var name: String?,
    var username: String?,
    var address: String?,
    var bio: String?,
    var email: String?,
    var phone: String?
) {
    fun isValid(): Boolean {
        return name != null || address != null || bio != null || email != null || username != null || phone != null
    }
}
