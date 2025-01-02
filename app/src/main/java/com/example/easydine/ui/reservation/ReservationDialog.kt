package com.example.easydine.ui.reservation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.easydine.data.network.request.FoodItem
import com.example.easydine.data.network.request.OrderRequest
import com.example.easydine.databinding.FragmentReservationBinding
import com.example.easydine.ui.viewmodel.FoodViewModel
import com.example.easydine.ui.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReservationDialog : DialogFragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val reservationData = mutableMapOf<String, String>()
    private val calendar = Calendar.getInstance()

    private val foodViewModel: FoodViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDatePicker()
        setupTimePicker()
        setupSubmitButton()
        observeOrderResult()
    }

    private fun setupDatePicker() {
        binding.btnPickDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)

                    val selectedDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    reservationData["start_date"] = selectedDate
                    binding.tvSelectedDate.text = "Selected Date: $selectedDate"
                },
                year,
                month,
                day
            )

            // Chặn chọn ngày trong quá khứ
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun setupTimePicker() {
        binding.btnPickTime.setOnClickListener {
            val now = Calendar.getInstance()
            val currentHour = now.get(Calendar.HOUR_OF_DAY)
            val currentMinute = now.get(Calendar.MINUTE)

            // Nếu là ngày hôm nay
            val isToday = reservationData["start_date"] ==
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())

            // Tính giờ mặc định (giờ hiện tại + 1, làm tròn lên giờ tiếp theo)
            val defaultHour = if (isToday) currentHour + 1 else 9 // Mặc định 9 giờ nếu không phải hôm nay
            val defaultMinute = 0 // Luôn làm tròn phút về 0

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    if (isToday && (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute < currentMinute))) {
                        // Thời gian không được chọn trước thời điểm hiện tại
                        showErrorDialog("Cannot select a time earlier than the current time.")
                    } else {
                        // Thiết lập giờ, phút, và giây
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                        calendar.set(Calendar.MINUTE, selectedMinute)
                        calendar.set(Calendar.SECOND, 0) // Đặt giây là 00

                        val formattedTime =
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
                        reservationData["start_time"] = formattedTime
                        binding.tvSelectedTime.text = "Selected Time: $formattedTime"
                    }
                },
                defaultHour,
                defaultMinute,
                true
            )
            timePickerDialog.show()
        }
    }



    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            val numPeopleText = binding.etNumPeople.text.toString()

            if (reservationData["start_date"].isNullOrEmpty() ||
                reservationData["start_time"].isNullOrEmpty() ||
                numPeopleText.isEmpty()
            ) {
                showErrorDialog("Please fill all fields correctly to proceed.")
                return@setOnClickListener
            }

            val numPeople = numPeopleText.toIntOrNull()
            if (numPeople == null || numPeople <= 0) {
                showErrorDialog("Please enter a valid number of people.")
                return@setOnClickListener
            }

            val isoDateTime = "${reservationData["start_date"]}T${reservationData["start_time"]}Z"

            // Lấy giá trị của cartItems một lần
            val cartItems = foodViewModel.cartItems.value ?: emptyList()

            val foods = cartItems.map { food ->
                FoodItem(
                    id = food.id,
                    quantity = food.quantity
                )
            }

            val orderRequest = OrderRequest(
                type = "reservation",
                status = "pending",
                start_time = isoDateTime,
                num_people = numPeople,
                foods = foods
            )

            // Tạo đơn hàng qua ViewModel
            orderViewModel.createOrder(orderRequest)
        }
    }


    private fun observeOrderResult() {
        orderViewModel.orderResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { orderResponse ->
                showSuccessDialog("Order created successfully with ID = ${orderResponse.id}")
                resetDialogData()
            }.onFailure { error ->
                showErrorDialog("Failed to create order: ${error.message}")
            }
        }
    }

    private fun resetDialogData() {
        reservationData.clear()
        binding.tvSelectedDate.text = "Selected Date: "
        binding.tvSelectedTime.text = "Selected Time: "
        binding.etNumPeople.text.clear()

        // Reset số lượng món ăn trong giỏ về 0
        foodViewModel.resetCartQuantities()
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                dismiss() // Close the dialog on success
            }
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
