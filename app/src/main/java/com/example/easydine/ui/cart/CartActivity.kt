package com.example.easydine.ui.cart

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydine.databinding.ActivityCartBinding
import com.example.easydine.ui.adapter.CartAdapter
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
        val cartAdapter = CartAdapter(
            emptyList(),
            onIncreaseQuantity = { foodId ->
                foodViewModel.increaseQuantity(foodId)
            },
            onDecreaseQuantity = { foodId ->
                foodViewModel.decreaseQuantity(foodId)
            }
        )
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter

        // Observe cart items and update the adapter
        foodViewModel.cartItems.observe(this, Observer { cartFoods ->
            if (cartFoods != null && cartFoods.isNotEmpty()) {
                cartAdapter.setData(cartFoods) // Cập nhật dữ liệu giỏ hàng
                foodViewModel.calculateTotalPrice(cartFoods)
            } else {
                // Hiển thị thông báo nếu giỏ hàng trống
//                binding.tvEmptyCartMessage.visibility = View.VISIBLE
            }
        })

        // Quan sát tổng tiền
        foodViewModel.totalPrice.observe(this, Observer { total ->
            binding.tvTotalPrice.text = "Total: ${String.format("%.0f", total)} VND"
        })
    }
}

