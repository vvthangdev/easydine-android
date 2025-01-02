package com.example.easydine.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easydine.databinding.FragmentOrderBinding
import com.example.easydine.ui.order.OrderAdapter
import com.example.easydine.ui.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderAdapter: OrderAdapter
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Sử dụng ViewBinding
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView Adapter
        orderViewModel.allOrders.observe(viewLifecycleOwner, { orders ->
            orderAdapter = OrderAdapter(orders)
            binding.recyclerViewOrders.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewOrders.adapter = orderAdapter
        })

        // Fetch data
        orderViewModel.fetchAndSaveOrders()
    }
}
