package com.example.easydine.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easydine.data.model.User
import com.example.easydine.data.repositories.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userData = MutableLiveData<User>()   // MutableLiveData cho phép thay đổi dữ liệu.
    val userData: LiveData<User> get() = _userData    // LiveData chỉ cho phép quan sát, không thay đổi.


    // Lấy dữ liệu người dùng từ SharedPreferences
    fun getUserData() {
        _userData.value = userRepository.getUserData()
    }

    fun logOut() {
        userRepository.clearUserData()
    }


    // Cập nhật dữ liệu người dùng
//    fun updateUserData(userData: User) {
//        userRepository.updateUserData(userData)
//        loadUserData()  // Tải lại dữ liệu sau khi cập nhật
//    }
}
