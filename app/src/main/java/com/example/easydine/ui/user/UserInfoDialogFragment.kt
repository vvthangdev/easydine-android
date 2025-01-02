package com.example.easydine.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.easydine.data.model.UserUpdateRequest
import com.example.easydine.databinding.FragmentUserInfoDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentUserInfoDialogBinding
    private val userViewModel: UserViewModel by activityViewModels()

    private val TAG = "UserInfoDialogFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoDialogBinding.inflate(inflater, container, false)

        // Lấy thông tin người dùng từ ViewModel

        userViewModel.getUserData()

        // Quan sát thay đổi dữ liệu người dùng từ ViewModel
        userViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Ghi lại thông tin người dùng vào Log
                Log.d(TAG, "User data received: name=${it.name}, address=${it.address}, bio=${it.bio}, email=${it.email}, username=${it.username}, phone=${it.phone}")

                // Cập nhật thông tin người dùng vào các trường nhập liệu
                binding.edtFullName.setText(it.name)
                binding.edtAddress.setText(it.address)
                binding.edtBio.setText(it.bio)  // Cập nhật đúng trường bio
                binding.edtEmail.setText(it.email)
                binding.edtUserName.setText(it.username)
                binding.edtPhone.setText(it.phone)
            }
        }

        // Xử lý sự kiện khi bấm nút cập nhật
        binding.btnUpdate.setOnClickListener {
            // Lấy dữ liệu từ các trường nhập và chỉ gửi những trường không rỗng
            val name = binding.edtFullName.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()
            val bio = binding.edtBio.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val username = binding.edtUserName.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()

            // Ghi lại thông tin trường trước khi gửi yêu cầu cập nhật
            Log.d(TAG, "Update request: name=$name, address=$address, bio=$bio, email=$email, username=$username, phone=$phone")

            // Xây dựng đối tượng UserUpdateRequest chỉ với các trường có dữ liệu
            val userUpdateRequest = UserUpdateRequest(
                name = if (name.isNotEmpty()) name else null,
                address = if (address.isNotEmpty()) address else null,
                bio = if (bio.isNotEmpty()) bio else null,
                email = if (email.isNotEmpty()) email else null,
                username = if (username.isNotEmpty()) username else null,
                phone = if (phone.isNotEmpty()) phone else null
            )

            // Ghi lại dữ liệu userUpdateRequest để kiểm tra
            Log.d(TAG, "UserUpdateRequest: $userUpdateRequest")

            // Gửi yêu cầu cập nhật chỉ khi có ít nhất một trường được điền
            if (userUpdateRequest.isValid()) {
                // Cập nhật thông tin người dùng
                userViewModel.updateUserData(userUpdateRequest)

                // Quan sát trạng thái cập nhật
                userViewModel.updateStatus.observe(viewLifecycleOwner, { isSuccess ->
                    if (isSuccess) {
                        // Hiển thị thông báo thành công
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Update successful!")

                        // Sau khi cập nhật thành công, làm mới dữ liệu người dùng
                        userViewModel.refreshUserData()

                        dismiss()  // Đóng dialog sau khi hiển thị thông báo
                    } else {
                        // Hiển thị thông báo thất bại
                        Toast.makeText(context, "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Update failed.")
                    }
                })
            } else {
                Toast.makeText(context, "Vui lòng nhập ít nhất một thông tin để cập nhật.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "No data to update.")
            }
        }


        return binding.root
    }

    companion object {
        fun newInstance(): UserInfoDialogFragment {
            return UserInfoDialogFragment()
        }
    }
}
