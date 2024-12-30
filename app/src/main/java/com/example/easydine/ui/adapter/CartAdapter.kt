package com.example.easydine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydine.data.model.Food
import com.example.easydine.databinding.ItemCartBinding

class CartAdapter(private var foods: List<Food>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

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

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.tvFoodName.text = food.name
            binding.tvFoodPrice.text = "${food.price} VND"

            // Sử dụng Glide để tải hình ảnh từ URL
            Glide.with(binding.root.context)
                .load(food.image) // Tải hình ảnh từ URL
                .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Placeholder khi hình ảnh đang tải
                .error(android.R.drawable.stat_notify_error) // Hình ảnh lỗi nếu không tải được
                .into(binding.ivFoodImage) // Đặt hình ảnh vào ImageView
        }
    }
}

