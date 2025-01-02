package com.example.easydine.ui.cart

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydine.databinding.ActivityCartBinding
import com.example.easydine.ui.adapter.CartAdapter
import com.example.easydine.ui.reservation.ReservationDialog
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
            },
            onUpDateQuantity = { foodId, quantity ->
                foodViewModel.updateQuantity(foodId, quantity)
            }
        )
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter
        binding.btnBack.setOnClickListener { finish() }

        // Observe cart items and update the adapter
        foodViewModel.cartItems.observe(this, Observer { cartFoods ->
            if (cartFoods != null && cartFoods.isNotEmpty()) {
                cartAdapter.setData(cartFoods) // Cập nhật dữ liệu giỏ hàng
                foodViewModel.calculateTotalPrice(cartFoods)
            } else {
                cartAdapter.setData(cartFoods)
                // Hiển thị thông báo nếu giỏ hàng trống
//                binding.tvEmptyCartMessage.visibility = View.VISIBLE
            }
        })

        // Quan sát tổng tiền
        foodViewModel.totalPrice.observe(this, Observer { total ->
            binding.tvTotalPrice.text = "Total: ${String.format("%.0f", total)} VND"
        })

        binding.btnPlaceOrder.setOnClickListener {
            showReservationDialog()
        }
    }

    private fun showReservationDialog() {
        val reservationDialog = ReservationDialog()
        reservationDialog.show(supportFragmentManager, "ReservationDialog")
    }
}

