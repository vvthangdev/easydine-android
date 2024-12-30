package com.example.easydine.utils

import android.content.Context
import android.widget.Toast

object ToastManager {
    private var currentToast: Toast? = null

    fun showToast(context: Context, message: String) {
        // Hủy toast hiện tại nếu có
        currentToast?.cancel()

        // Tạo và hiển thị toast mới
        currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}