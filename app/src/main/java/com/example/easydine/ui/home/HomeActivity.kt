package com.example.easydine.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easydine.databinding.ActivityHomeBinding
import com.example.easydine.ui.adapter.FoodAdapter
import com.example.easydine.ui.adapter.ImageBannerAdapter
import com.example.easydine.ui.cart.CartActivity
import com.example.easydine.ui.login.LoginActivity
import com.example.easydine.ui.reservation.ReservationDialog
import com.example.easydine.ui.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val imageBannerViewModel: ImageBannerViewModel by viewModels()
    private val foodViewModel: FoodViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

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
            AlertDialog.Builder(this)
                .setTitle("Authentication required")
                .setMessage("Access token is null. Please login to continue.")
                .setPositiveButton("Login") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

        observeViewModel()

        binding.ivCartIcon.setOnClickListener {
            // Mở CartActivity
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.btnReservation.setOnClickListener {
            lifecycleScope.launch {
                val cartItems = homeViewModel.getCartItemsSync()
                if (cartItems.isNotEmpty()) {
                    // Có món trong giỏ hàng -> Mở CartActivity
                    val intent = Intent(this@HomeActivity, CartActivity::class.java)
                    startActivity(intent)
                } else {
                    // Không có món trong giỏ hàng -> Hiển thị ReservationFragment
                    showReservationDialog()
                }
            }
        }


//        binding.btnReservation.setOnClickListener{showReservationFragment()}
    }

    private fun showReservationDialog() {
        val reservationDialog = ReservationDialog()
        reservationDialog.show(supportFragmentManager, "ReservationDialog")
    }

    override fun onBackPressed() {
        // Handle back navigation
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    /*
    Banner
    */

    private fun setupAdapters() {
        // Khởi tạo adapter cho banners
        imageBannerAdapter = ImageBannerAdapter(emptyList())
        binding.vpImageBanner.adapter = imageBannerAdapter

        // Khởi tạo adapter cho danh sách món ăn
//        foodAdapter = FoodAdapter { foodId ->
//            foodViewModel.addToCart(foodId, 1)
//        }

        foodAdapter = FoodAdapter(
            onAddToCartClick = { foodId ->
                foodViewModel.increaseQuantity(foodId)
            }
        )
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
                currentPage =
                    if (currentPage == imageBannerAdapter.itemCount - 1) 0 else currentPage + 1
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
