package com.example.easydine.ui.user

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easydine.R
import com.example.easydine.databinding.FragmentUserBinding
import com.example.easydine.ui.login.LoginActivity
import com.example.easydine.ui.order.OrderFragment
import com.example.easydine.ui.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
//    private lateinit var orderViewModel: OrderViewModel
    private val orderViewModel: OrderViewModel by viewModels()

    // Các view bạn cần tham chiếu tới
    private lateinit var ivUserAvatar: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserBio: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentUserBinding.inflate(inflater, container, false)

        ivUserAvatar = binding.ivUserAvatar
        tvUserName = binding.tvUserName
        tvUserBio = binding.tvUserBio
        binding.itemProfile.layoutLogOutRow.setOnClickListener {
            showLogOutDialog()
        }

        binding.itemProfile.layoutPersonalInfoRow.setOnClickListener {
            onPersionalInfoClicked()
        }

        binding.itemProfile.layoutCartRow.setOnClickListener {
            onCartInfoClicked()
        }

        return binding.root
    }

    private fun onCartInfoClicked() {
        orderViewModel.fetchAndSaveOrders()
        val orderFragment = OrderFragment()

        // Thay thế Fragment hiện tại bằng OrderFragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, orderFragment) // ID của container trong layout chính
            .addToBackStack(null) // Thêm vào back stack để người dùng có thể quay lại
            .commit()
    }

    private fun onPersionalInfoClicked() {
        // Làm mới dữ liệu người dùng từ ViewModel
        userViewModel.getUserData()

        // Tạo instance của UserInfoDialogFragment
        val userInfoDialogFragment = UserInfoDialogFragment()

        // Mở fragment mà không cần truyền dữ liệu qua Bundle
        userInfoDialogFragment.show(childFragmentManager, "UserInfoDialogFragment")
    }


    // Hàm hiển thị AlertDialog khi người dùng chọn đăng xuất
    private fun showLogOutDialog() {
        // Tạo AlertDialog xác nhận đăng xuất
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất không?")
            .setCancelable(false)  // Không cho phép đóng ngoài vùng ngoài dialog
            .setPositiveButton("Đồng ý") { dialog, id ->
                // Xử lý khi người dùng chọn "Đồng ý"
                userViewModel.logOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Hủy") { dialog, id ->
                // Xử lý khi người dùng chọn "Hủy"
                dialog.dismiss()  // Đóng dialog
            }

        // Hiển thị AlertDialog
        builder.create().show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

//        userViewModel.refreshUserData()
        userViewModel.getUserData()

        // Quan sát dữ liệu người dùng
        userViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            // Cập nhật UI với thông tin người dùng
            user?.let {
                tvUserName.text = it.name
                tvUserBio.text = it.bio // Nếu có tagline, nếu không có thì có thể để trống

                // Sử dụng Glide hoặc thư viện tương tự để tải ảnh avatar
                Glide.with(requireContext())
                    .load(it.avatar)  // Đảm bảo rằng `avatar` là URL hoặc đường dẫn hợp lệ
                    .into(ivUserAvatar)
            }
        })
    }
}
