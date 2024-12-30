package com.example.easydine.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easydine.R
import com.example.easydine.data.model.Food

class FoodAdapter(
    private val onAddToCartClick: (Int) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private var foods = emptyList<Food>()

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val addToCartButton: ImageButton = itemView.findViewById(R.id.addToCartButton)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]

        Glide.with(holder.itemView.context)
            .load(food.image)
            .placeholder(R.drawable.launch_foods)
            .into(holder.foodImage)

        holder.foodName.text = food.name
        holder.foodPrice.text = "${food.price.toInt()} VND"

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(food.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    fun setData(newFoods: List<Food>) {
        this.foods = newFoods
        notifyDataSetChanged()
    }
}
