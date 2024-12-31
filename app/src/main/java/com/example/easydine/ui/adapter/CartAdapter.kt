package com.example.easydine.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydine.data.model.Food
import com.example.easydine.databinding.ItemCartBinding

class CartAdapter(
    private var foods: List<Food>,
    private val onIncreaseQuantity: (Int) -> Unit,
    private val onDecreaseQuantity: (Int) -> Unit,
    private val onUpDateQuantity: (Int, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int = foods.size

    // Cập nhật dữ liệu mới
    fun setData(newFoods: List<Food>) {
        foods = newFoods
        notifyDataSetChanged()
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvFoodPrice.text = "${food.price} VND"
            binding.etQuantity.setText(food.quantity.toString())

            // Nút tăng số lượng
            binding.btnIncreaseQuantity.setOnClickListener {
                Log.d(
                    "CartAdapter",
                    "Tăng số lượng món ăn: ${food.name}, ID: ${food.id}, Giá: ${food.price}, Số lượng hiện tại: ${food.quantity}"
                )
                onIncreaseQuantity(food.id)
            }

            binding.etQuantity.setText(food.quantity.toString())

            // Nút giảm số lượng
            binding.btnDecreaseQuantity.setOnClickListener {
                Log.d(
                    "CartAdapter",
                    "Giảm số lượng món ăn: ${food.name}, ID: ${food.id}, Giá: ${food.price}, Số lượng hiện tại: ${food.quantity}"
                )
                if (food.quantity == 1) {
                    AlertDialog.Builder(binding.root.context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn xóa món này khỏi giỏ hàng?")
                        .setPositiveButton("Xóa") { _, _ ->
                            onDecreaseQuantity(food.id)
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                } else {
                    onDecreaseQuantity(food.id)
                }
            }

            binding.etQuantity.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) { // Khi EditText mất focus
                    val enteredQuantity = binding.etQuantity.text.toString().toIntOrNull() ?: 0
                    if (enteredQuantity != food.quantity) {
                        onUpDateQuantity(food.id, enteredQuantity)
                    }
                }
            }


//            binding.etQuantity.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    val enteredQuantity = s.toString().toIntOrNull() ?: return
//                    if (enteredQuantity != food.quantity) {
////                        onUpdateQuantity(food.id, enteredQuantity)
//                        onUpDateQuantity(food.id, enteredQuantity)
//                    }
//                }
//
//                override fun afterTextChanged(s: Editable?) {}
//            })


            // Sử dụng Glide để tải hình ảnh từ URL
            Glide.with(binding.root.context)
                .load(food.image) // Tải hình ảnh từ URL
                .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Placeholder khi hình ảnh đang tải
                .error(android.R.drawable.stat_notify_error) // Hình ảnh lỗi nếu không tải được
                .into(binding.ivFoodImage) // Đặt hình ảnh vào ImageView
        }
    }
}

