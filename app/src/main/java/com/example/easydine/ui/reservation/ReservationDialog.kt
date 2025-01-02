package com.example.easydine.ui.reservation

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import com.example.easydine.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReservationDialog : DialogFragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val reservationData = mutableMapOf<String, String>()
    private val calendar = Calendar.getInstance()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var preferenceManager: PreferenceManager
    private var lastSubmitTime: Long = 0


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

        // Khởi tạo PreferenceManager
        preferenceManager = PreferenceManager(requireContext())
        // Lấy giá trị lastSubmitTime từ SharedPreferences
        lastSubmitTime = preferenceManager.getLastSubmitTime()

        setupDefaultValues()
        setupCalendarView()
        setupTimePicker()
        setupSubmitButton()
        observeOrderResult()
    }

    private fun setupDefaultValues() {
        // Đặt ngày mặc định là hôm nay
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
        reservationData["start_date"] = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time) // Lưu định dạng ISO
//        binding.tvSelectedDate.text = "Selected Date: $currentDate"

        // Đặt giờ mặc định là giờ hiện tại + 1
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val defaultTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
        reservationData["start_time"] = defaultTime
//        binding.btnPickTime.text = "Reservation Time: $defaultTime"
    }

    private fun setupCalendarView() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)

            val selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
            reservationData["start_date"] = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time) // Lưu định dạng ISO

            // Chặn chọn ngày trong quá khứ
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                showErrorDialog("Cannot select a date in the past.")
//                binding.tvSelectedDate.text = "Selected Date: None"
                reservationData.remove("start_date")
            }
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
                        calendar.set(Calendar.SECOND, 0)

                        val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.time)
                        reservationData["start_time"] = formattedTime
                        binding.btnPickTime.text = "Reservation Time: $formattedTime"
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
            val currentTime = System.currentTimeMillis()


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
                Log.d("ReservationDialog", "Validation failed: Invalid number of people.")
                return@setOnClickListener
            }

            val isoDateTime = "${reservationData["start_date"]}T${reservationData["start_time"]}Z"
            Log.d("ReservationDialog", "ISO DateTime: $isoDateTime")

            val cartItems = foodViewModel.cartItems.value ?: emptyList()
            Log.d("ReservationDialog", "Cart items: ${cartItems.size}")

            val foods = cartItems.map { food ->
                FoodItem(
                    id = food.id,
                    quantity = food.quantity
                )
            }
            Log.d("ReservationDialog", "Foods: $foods")

            val orderRequest = OrderRequest(
                type = "reservation",
                status = "pending",
                start_time = isoDateTime,
                num_people = numPeople,
                foods = foods
            )
            Log.d("ReservationDialog", "OrderRequest: $orderRequest")

            // Kiểm tra nếu 60 giây chưa trôi qua
            if (currentTime - preferenceManager.getLastSubmitTime() < 60000) {
                showErrorDialog("Please wait before submitting again.")
                Log.d("ReservationDialog", "Submit blocked: Please wait.")
                return@setOnClickListener
            }

            preferenceManager.saveLastSubmitTime(currentTime)
            Log.d("ReservationDialog", "Submit allowed: Proceeding with createOrder.")

            orderViewModel.createOrder(orderRequest)
            disableButtonForOneMinute()
        }
    }

//    private fun disableButtonForOneMinute() {
//        binding.btnSubmit.isEnabled = false
//
//        // Sử dụng CountDownTimer để kích hoạt lại nút sau 60 giây
//        object : CountDownTimer(60000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.btnSubmit.text = "Wait: ${millisUntilFinished / 1000}s"
//            }
//
//            override fun onFinish() {
//                binding.btnSubmit.isEnabled = true
//                binding.btnSubmit.text = "Submit Reservation"
//            }
//        }.start()
//    }

    private fun disableButtonForOneMinute() {
        binding.btnSubmit.isEnabled = false

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding?.let {
                    it.btnSubmit.text = "Wait: ${millisUntilFinished / 1000}s"
                }
            }

            override fun onFinish() {
                binding?.let {
                    it.btnSubmit.isEnabled = true
                    it.btnSubmit.text = "Submit Reservation"
                }
            }
        }
        countDownTimer?.start()
    }

    private fun observeOrderResult() {
        orderViewModel.orderResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                it.onSuccess { orderResponse ->
                    showSuccessDialog("Order created successfully with ID = ${orderResponse.id}")
                    resetDialogData()
                }.onFailure { error ->
                    showErrorDialog("Failed to create order: ${error.message}")
                }
                // Đặt lại giá trị sau khi xử lý
                orderViewModel.clearOrderResult()
            }
        }
    }


    private fun resetDialogData() {
        reservationData.clear()
//        setupDefaultValues() // Reset lại giá trị mặc định
        binding.etNumPeople.text.clear()

        foodViewModel.resetCartQuantities()
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                dismiss()
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
        countDownTimer?.cancel() // Hủy CountDownTimer nếu Dialog bị đóng
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
