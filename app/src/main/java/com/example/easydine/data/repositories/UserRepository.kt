package com.example.easydine.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.easydine.data.model.User
import com.example.easydine.data.model.UserUpdateRequest
import com.example.easydine.data.network.response.LoginResponse
import com.example.easydine.data.network.response.RefreshTokenResponse
import com.example.easydine.data.network.response.SignUpResponse
import com.example.easydine.data.network.response.UserUpdateResponse
import com.example.easydine.data.network.service.UserApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val apiService: UserApiService
) {


    // Lưu thông tin người dùng vào SharedPreferences
    private fun saveUserData(user: User) {
        val editor = sharedPreferences.edit()

        // Chỉ lưu các giá trị nếu có thay đổi, không cần lưu lại những trường null
        editor.putInt("id", user.id ?: -1)
        editor.putString("role", user.role)
        editor.putString("name", user.name)
        editor.putString("status", user.status)
        editor.putString("message", user.message)
        editor.putString("address", user.address)
        editor.putString("avatar", user.avatar)
        editor.putString("bio", user.bio)
        editor.putString("email", user.email)
        editor.putString("phone", user.phone)
        editor.putString("username", user.username)
        editor.putString("accessToken", user.accessToken)
        editor.putString("refreshToken", user.refreshToken)

        editor.apply()  // Lưu thay đổi
    }


    // Lấy dữ liệu người dùng từ SharedPreferences
    fun getUserData(): User? {
        val id = sharedPreferences.getInt("id", -1)
        val role = sharedPreferences.getString("role", null)
        val name = sharedPreferences.getString("name", null)
        val status = sharedPreferences.getString("status", null)
        val message = sharedPreferences.getString("message", null)
        val address = sharedPreferences.getString("address", null)
        val avatar = sharedPreferences.getString("avatar", null)
        val bio = sharedPreferences.getString("bio", null)
        val email = sharedPreferences.getString("email", null)
        val phone = sharedPreferences.getString("phone", null)
        val username = sharedPreferences.getString("username", null)
        val accessToken = sharedPreferences.getString("accessToken", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)

        // Nếu tất cả thông tin người dùng hợp lệ, trả về User, nếu không trả về null
        return if (role != null && name != null && email != null && accessToken != null) {
            User(
                id = id,
                role = role,
                name = name,
                status = status,
                message = message,
                address = address,
                avatar = avatar,
                bio = bio,
                email = email,
                phone = phone,
                username = username,
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } else {
            null
        }
    }

    // Hàm này sẽ được gọi khi dữ liệu người dùng được tải từ API
    private fun updateUserIfNeeded(user: User) {
        val currentUser = getUserData()  // Lấy dữ liệu người dùng hiện tại từ SharedPreferences

        val updatedUser = currentUser?.copy(
            // Chỉ cập nhật nếu có dữ liệu trả về
            role = user.role ?: currentUser.role,
            name = user.name ?: currentUser.name,
            status = user.status ?: currentUser.status,
            message = user.message ?: currentUser.message,
            address = user.address ?: currentUser.address,
            avatar = user.avatar ?: currentUser.avatar,
            bio = user.bio ?: currentUser.bio,
            email = user.email ?: currentUser.email,
            phone = user.phone ?: currentUser.phone,
            username = user.username ?: currentUser.username,
            accessToken = user.accessToken ?: currentUser.accessToken,
            refreshToken = user.refreshToken ?: currentUser.refreshToken
        )

        // Sau khi cập nhật các trường hợp có dữ liệu, lưu lại vào SharedPreferences
        updatedUser?.let { saveUserData(it) }
    }


    // Xóa dữ liệu người dùng khỏi SharedPreferences
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear()  // Xóa tất cả dữ liệu
        editor.apply()
    }

    // Lấy dữ liệu người dùng từ API và lưu vào SharedPreferences
    suspend fun refreshUserData(): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserDataApi()

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Cập nhật người dùng chỉ với các trường có dữ liệu trả về
                        updateUserIfNeeded(user)
                        return@withContext user
                    }
                }
                return@withContext null
            } catch (e: Exception) {
                Log.e("UserRepository", "Error fetching user data: ${e.message}", e)
                return@withContext null
            }
        }
    }


    // Cập nhật thông tin người dùng
    suspend fun updateUserData(userUpdateRequest: UserUpdateRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateUserDataApi(userUpdateRequest)

                if (response.isSuccessful) {
                    // Nếu cập nhật thành công, trả về true
                    return@withContext true
                } else {
                    // Nếu có lỗi, trả về false
                    return@withContext false
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error updating user data: ${e.message}", e)
                return@withContext false
            }
        }
    }


    // Đăng nhập người dùng
    suspend fun loginUser(email: String, password: String): LoginResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.loginUser(email, password)

                if (response.isSuccessful) {
                    Log.d("UserRepository", "Login successful. Status: ${response.body()}")
                    return@withContext response.body()
                } else {
                    Log.e("UserRepository", "Login failed. Error code: ${response.code()} - ${response.message()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Login error: ${e.message}", e)
                return@withContext null
            }
        }
    }

    // Refresh access token
    suspend fun refreshAccessToken(refreshToken: String): RefreshTokenResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val authorizationHeader = "$refreshToken"
                Log.d("UserRepository", "Authorization: $authorizationHeader")

                val response = apiService.refreshAccessToken(authorizationHeader)

                if (response.isSuccessful) {
                    Log.d("UserRepository", "Refresh token successful. AccessToken: ${response.body()?.accessToken}")
                    return@withContext response.body()
                } else {
                    Log.e("UserRepository", "Refresh token failed. Error code: ${response.code()} - ${response.message()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Refresh token error: ${e.message}", e)
                return@withContext null
            }
        }
    }

    suspend fun signUpUser(
        email: String,
        name: String,
        username: String,
        phone: String,
        password: String
    ): SignUpResponse? {
        return try {
            val response = apiService.registerUser(email, name, username, phone, password)

            if (response.isSuccessful) {
                response.body()  // Trả về dữ liệu người dùng nếu đăng ký thành công
            } else {
                null
            }
        } catch (e: Exception) {
            null  // Trả về null trong trường hợp có lỗi (ví dụ: lỗi kết nối)
        }
    }
}
