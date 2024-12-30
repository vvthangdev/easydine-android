package com.example.easydine.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easydine.data.model.ImageBanner
import com.example.easydine.data.repositories.ImageBannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageBannerViewModel @Inject constructor(
    private val repository: ImageBannerRepository
) : ViewModel() {

    val banners: LiveData<List<ImageBanner>> = repository.getAllBanners()

    // Hàm làm mới dữ liệu từ API
    fun refreshBanners(authorization: String) {
        viewModelScope.launch {
            repository.refreshBanners(authorization)
        }
    }
}
