package com.example.easydine.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.easydine.data.model.Order
import com.example.easydine.databinding.ItemOrderBinding

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        // Pass the position (1-based index) to the ViewHolder
        holder.bind(orders[position], position + 1)
    }

    override fun getItemCount(): Int = orders.size

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        // Add an additional parameter for the position index
        fun bind(order: Order, position: Int) {
            binding.buttonOrderNumber.text = position.toString() // Set button number as the position index
            binding.textViewOrderId.text = "Order ID: ${order.id}"
            binding.textViewOrderTime.text = "Order Time: ${order.time}"
            binding.textViewOrderStatus.text = "Status: ${order.status}"
        }
    }
}
