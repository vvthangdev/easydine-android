package com.example.easydine.ui.signup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _signupRes = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> get() = _signupRes

    fun signUpUser(email: String, name: String, username: String, phone: String, password: String, context: Context) {
        if (email.isNotEmpty() && name.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val signUpResponse = userRepository.signUpUser(email, name, username, phone, password)
                    if (signUpResponse?.status == "SUCCESS") {
                        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", signUpResponse.data)
                        editor.apply()

                        // Hiển thị thông báo thành công
                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()

                        // Cập nhật kết quả thành công
                        _signupRes.postValue(SignUpResult.Success)
                    }
                } catch (e: HttpException) {
                    // Hiển thị lỗi HTTP
                    Toast.makeText(context, "Registration failed! Server error", Toast.LENGTH_SHORT).show()
                    _signupRes.postValue(SignUpResult.Failure("Registration failed! Server error"))
                } catch (e: Exception) {
                    // Hiển thị lỗi khác
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    _signupRes.postValue(SignUpResult.Failure("Error: ${e.message}"))
                }
            }
        } else {
            // Hiển thị thông báo lỗi khi thông tin không đầy đủ
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            _signupRes.postValue(SignUpResult.Failure("Please fill in all fields"))
        }
    }

}

sealed class SignUpResult {
    data object Success : SignUpResult()  // Đăng ký thành công
    data class Failure(val message: String) : SignUpResult()  // Đăng ký thất bại
}