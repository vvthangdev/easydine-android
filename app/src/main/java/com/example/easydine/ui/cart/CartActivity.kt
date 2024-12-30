package com.example.easydine.ui.cart

import com.example.easydine.ui.adapter.CartAdapter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydine.databinding.ActivityCartBinding
import com.example.easydine.ui.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val foodViewModel: FoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView setup
        val cartAdapter = CartAdapter(emptyList())
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter

        // Observe cart items and update the adapter
        foodViewModel.cartItems.observe(this, Observer { cartFoods ->
            if (cartFoods != null && cartFoods.isNotEmpty()) {
                cartAdapter.setData(cartFoods) // Cập nhật dữ liệu giỏ hàng
            } else {
                // Hiển thị thông báo nếu giỏ hàng trống
//                binding.tvEmptyCartMessage.visibility = View.VISIBLE
            }
        })
    }
}

