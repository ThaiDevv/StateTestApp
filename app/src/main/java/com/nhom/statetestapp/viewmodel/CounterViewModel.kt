package com.nhom.statetestapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ============================================================
 * CounterViewModel – ViewModel cho màn hình demo ViewModel
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 3 (TV3)
 * ============================================================
 *
 * File này đã có khung cơ bản. TV3 cần:
 * 1. KHÔNG xoá bất kỳ Log nào đã có (quan trọng để demo)
 * 2. Có thể thêm Log nếu cần
 * 3. Kiểm tra file này hoạt động đúng với ViewModelScreen.kt
 *
 * ⚠️ QUAN TRỌNG:
 * Log "ViewModel CREATED" và "ViewModel CLEARED" là BẰNG CHỨNG
 * quan trọng nhất trong test cases. Phải đảm bảo 2 log này
 * xuất hiện đúng lúc trong Logcat.
 *
 * hashCode() được dùng để chứng minh:
 * - Cùng instance = giá trị giống nhau (survive)
 * - Khác instance = ViewModel mới (bị huỷ và tạo lại)
 * ============================================================
 */
private const val TAG = "STATE_TEST"

class CounterViewModel : ViewModel() {

    // State lưu trong ViewModel (RAM, survive config change)
    var name by mutableStateOf("")
        private set

    var count by mutableIntStateOf(0)
        private set

    var choice by mutableStateOf(false)
        private set

    init {
        // Log này xuất hiện mỗi khi ViewModel được TẠO MỚI
        Log.d(TAG, "[ViewModel] ✅ ViewModel CREATED - instance: #${hashCode()}")
    }

    fun updateName(newName: String) {
        name = newName
        Log.d(TAG, "[ViewModel] updateName → '$newName' (vmHash=#${hashCode()})")
    }

    fun incrementCount() {
        count++
        Log.d(TAG, "[ViewModel] incrementCount → $count (vmHash=#${hashCode()})")
    }

    fun toggleChoice() {
        choice = !choice
        Log.d(TAG, "[ViewModel] toggleChoice → $choice (vmHash=#${hashCode()})")
    }

    override fun onCleared() {
        super.onCleared()
        // Log này xuất hiện mỗi khi ViewModel bị HUỶ
        Log.d(TAG, "[ViewModel] ❌ ViewModel CLEARED/DESTROYED - instance: #${hashCode()}")
    }
}
