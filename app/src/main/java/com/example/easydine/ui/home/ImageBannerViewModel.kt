package com.example.easydine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.local.database.AppDatabase
import com.example.easydine.data.model.ImageBanner
import com.example.easydine.data.repositories.ImageBannerRepository
import kotlinx.coroutines.launch

class ImageBannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository= ImageBannerRepository(application)

    val banners: LiveData<List<ImageBanner>> = repository.getAllBanners()

//    init {
//        // Khởi tạo DAO từ database singleton
//        val database = AppDatabase.getDatabase(context)
//        imageBannerDao = database.imageBannerDao()
//        imageBannerService = ApiClient.imageBannerService // Sử dụng ImageBannerService từ ApiClient
//    }

    // Hàm làm mới dữ liệu từ API
    fun refreshBanners(authorization: String) {
        viewModelScope.launch {
            repository.refreshBanners(authorization)
        }
    }
}
