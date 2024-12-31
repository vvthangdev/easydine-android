package com.example.easydine.data.repositories

import androidx.lifecycle.LiveData
import com.example.easydine.data.local.dao.ImageBannerDao
import com.example.easydine.data.model.ImageBanner
import com.example.easydine.data.network.service.ImageBannerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageBannerRepository @Inject constructor(
    private val imageBannerDao: ImageBannerDao,
    private val imageBannerService: ImageBannerService
) {

    // Lấy tất cả banner từ database
    fun getAllBanners(): LiveData<List<ImageBanner>> {
        return imageBannerDao.getAllBanners()
    }

    // Làm mới dữ liệu từ API và lưu vào Room
    suspend fun refreshBanners(authorization: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = imageBannerService.getAllItemBanners(authorization)
                if (response.isSuccessful) {
                    response.body()?.let { bannerResponseList ->
                        val banners = bannerResponseList.map { response ->
                            ImageBanner(
                                id = response.id,
                                image = response.image,
                                title = response.title ?: "No Title" // Default title if missing
                            )
                        }
                        // Xóa dữ liệu cũ và thêm mới vào Room
                        imageBannerDao.deleteAllBanners()
                        imageBannerDao.insertBanners(banners)
                    }
                } else {
                    throw Exception("Failed to fetch banners: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
