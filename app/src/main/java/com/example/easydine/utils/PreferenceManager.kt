package com.example.easydine.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "ReservationPrefs"
        private const val KEY_LAST_SUBMIT_TIME = "last_submit_time"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Lưu thời gian lần submit gần nhất
    fun saveLastSubmitTime(time: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_SUBMIT_TIME, time).apply()
    }

    // Lấy thời gian lần submit gần nhất
    fun getLastSubmitTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_SUBMIT_TIME, 0)
    }

    // Thêm các phương thức khác nếu cần
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

}
