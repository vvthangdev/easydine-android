package com.example.easydine.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easydine.databinding.ActivityHomeBinding
import com.example.easydine.ui.adapter.FoodAdapter
import com.example.easydine.ui.adapter.ImageBannerAdapter
import com.example.easydine.ui.cart.CartActivity
import com.example.easydine.ui.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val imageBannerViewModel: ImageBannerViewModel by viewModels()
    private val foodViewModel: FoodViewModel by viewModels()

    private lateinit var imageBannerAdapter: ImageBannerAdapter
    private lateinit var foodAdapter: FoodAdapter

    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var autoScrollRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        initializeAutoScrollRunnable()

        // Fetch dữ liệu từ API nếu có accessToken
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken != null) {
            imageBannerViewModel.refreshBanners(accessToken)
            foodViewModel.fetchAndSaveFoods()
        } else {
            Log.e("HomeActivity", "Access token is null. Please login.")
            // Gợi ý: Thông báo cho người dùng hoặc chuyển đến màn hình đăng nhập
        }

        observeViewModel()

        binding.ivCartIcon.setOnClickListener {
            // Mở CartActivity
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAdapters() {
        // Khởi tạo adapter cho banners
        imageBannerAdapter = ImageBannerAdapter(emptyList())
        binding.vpImageBanner.adapter = imageBannerAdapter

        // Khởi tạo adapter cho danh sách món ăn
        foodAdapter = FoodAdapter { foodId ->
            foodViewModel.addToCart(foodId)
        }
        binding.rvFoodList.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = foodAdapter
        }
    }

    private fun observeViewModel() {
        // Quan sát banners
        imageBannerViewModel.banners.observe(this) { banners ->
            if (banners != null && banners.isNotEmpty()) {
                imageBannerAdapter.setData(banners) // Cập nhật dữ liệu banner
                startAutoScroll()
            }
        }

        // Quan sát danh sách món ăn
        foodViewModel.foods.observe(this) { foods ->
            foods?.let {
                foodAdapter.setData(it)
            }
        }

        // Quan sát số lượng món trong giỏ hàng
        foodViewModel.cartItemCount.observe(this) { count ->
            binding.tvCartBadge.text = count.toString()
            binding.tvCartBadge.visibility = if (count > 0) View.VISIBLE else View.GONE
        }
    }

    private fun initializeAutoScrollRunnable() {
        autoScrollRunnable = Runnable {
            if (::imageBannerAdapter.isInitialized && imageBannerAdapter.itemCount > 0) {
                currentPage = if (currentPage == imageBannerAdapter.itemCount - 1) 0 else currentPage + 1
                binding.vpImageBanner.setCurrentItem(currentPage, true)
                handler.postDelayed(autoScrollRunnable, 3000)
            }
        }
    }

    private fun startAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable) // Remove existing callbacks
        handler.postDelayed(autoScrollRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (::imageBannerAdapter.isInitialized && imageBannerAdapter.itemCount > 0) {
            startAutoScroll()
        }
    }
}
