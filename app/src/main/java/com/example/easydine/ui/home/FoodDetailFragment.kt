package com.example.easydine.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.easydine.databinding.FragmentFoodDetailBinding
import com.example.easydine.data.model.Food

class FoodDetailFragment : Fragment() {

//    private var _binding: FragmentFoodDetailBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Lấy dữ liệu món ăn từ arguments
//        val food = arguments?.getParcelable<Food>("food")
//
//        food?.let {
//            // Hiển thị thông tin món ăn
//            Glide.with(requireContext()).load(it.imageUrl).into(binding.ivFoodImage)
//            binding.tvFoodName.text = it.name
//            binding.tvFoodPrice.text = "${it.price} VND"
//            binding.tvFoodDescription.text = it.description
//
//            // Nút thêm vào giỏ hàng
//            binding.btnAddToCart.setOnClickListener {
//                // Xử lý thêm món ăn vào giỏ hàng (nếu cần)
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}