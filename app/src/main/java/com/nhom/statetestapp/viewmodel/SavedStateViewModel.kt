package com.nhom.statetestapp.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * ============================================================
 * SavedStateViewModel – ViewModel dùng SavedStateHandle
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 4 (TV4)
 * ============================================================
 *
 * File này đã có khung cơ bản. TV4 cần:
 * 1. KHÔNG xoá bất kỳ Log nào đã có
 * 2. Kiểm tra file này hoạt động đúng với SavedStateScreen.kt
 *
 * ⭐ ĐIỂM ĐẶC BIỆT CẦN CHÚ Ý:
 * Trong init block, log "Restored values" sẽ cho thấy:
 * - Nếu đây là lần KHỞI ĐỘNG MỚI → các giá trị = "" / 0 / false
 * - Nếu app được PHỤC HỒI sau process death → các giá trị = dữ liệu cũ
 * → Đây là BẰNG CHỨNG SavedStateHandle survive process death!
 *
 * Log "ViewModel CREATED" kết hợp với hashCode() sẽ chứng minh
 * đây là instance mới (sau process death), nhưng data được restore.
 * ============================================================
 */
private const val TAG = "STATE_TEST"

class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_NAME   = "ssh_name"
        private const val KEY_COUNT  = "ssh_count"
        private const val KEY_CHOICE = "ssh_choice"
    }

    // StateFlow tự động cập nhật UI khi data thay đổi
    val name:   StateFlow<String>  = savedStateHandle.getStateFlow(KEY_NAME, "")
    val count:  StateFlow<Int>     = savedStateHandle.getStateFlow(KEY_COUNT, 0)
    val choice: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_CHOICE, false)

    init {
        Log.d(TAG, "[SavedStateHandle] ✅ ViewModel CREATED - instance: #${hashCode()}")
        // Log này quan trọng: nếu có dữ liệu ở đây sau process death → SSH hoạt động!
        Log.d(TAG, "[SavedStateHandle] 📦 Restored values → " +
                "name='${name.value}', count=${count.value}, choice=${choice.value}")
    }

    fun updateName(newName: String) {
        savedStateHandle[KEY_NAME] = newName
        Log.d(TAG, "[SavedStateHandle] saveName → '$newName' (vmHash=#${hashCode()})")
    }

    fun incrementCount() {
        val newCount = count.value + 1
        savedStateHandle[KEY_COUNT] = newCount
        Log.d(TAG, "[SavedStateHandle] saveCount → $newCount (vmHash=#${hashCode()})")
    }

    fun toggleChoice() {
        val newChoice = !choice.value
        savedStateHandle[KEY_CHOICE] = newChoice
        Log.d(TAG, "[SavedStateHandle] saveChoice → $newChoice (vmHash=#${hashCode()})")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "[SavedStateHandle] ❌ ViewModel CLEARED - instance: #${hashCode()}")
    }
}
