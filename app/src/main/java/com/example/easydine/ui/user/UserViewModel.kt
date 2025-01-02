package com.example.easydine.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.User
import com.example.easydine.data.model.UserUpdateRequest
import com.example.easydine.data.repositories.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userData = MutableLiveData<User?>()   // MutableLiveData cho phép thay đổi dữ liệu.
    val userData: LiveData<User?> get() = _userData    // LiveData chỉ cho phép quan sát, không thay đổi.

    // Thêm LiveData cho trạng thái cập nhật
    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> = _updateStatus

    // Lấy dữ liệu người dùng từ SharedPreferences
    fun getUserData() {
        // Giả sử bạn lấy dữ liệu từ API hoặc SharedPreferences
        val user = userRepository.getUserData()
        Log.d("UserViewModel", "User data loaded: $user")
        _userData.value = user
    }

    fun logOut() {
        userRepository.clearUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.refreshUserData()
        }
    }

    // Cập nhật dữ liệu người dùng
    fun updateUserData(userUpdateRequest: UserUpdateRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isUpdated = userRepository.updateUserData(userUpdateRequest)

                _updateStatus.postValue(isUpdated)
            } catch (e: Exception) {
                _updateStatus.postValue(false)
            }
        }
    }

}
