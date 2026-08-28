package com.nhom.statetestapp.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * ============================================================
 * SavedStateViewModel – ViewModel dùng SavedStateHandle
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 4 (TV4)
 * ============================================================
 * Log "Restored values" trong init là BẰNG CHỨNG quan trọng:
 *   - Giá trị = default ("", 0, false) → lần đầu mở
 *   - Giá trị = data cũ → được restore sau kill process!
 *
 * Kết hợp với VM Hashcode:
 *   - Cùng hash + data cũ  → survive config change
 *   - Hash MỚI + data cũ  → VM mới nhưng SSH restore từ Bundle
 *   - Hash MỚI + data mặc định → VM mới, không restore được
 * ============================================================
 */
private const val TAG = "STATE_TEST"

class SavedStateViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_NAME   = "ssh_name"
        private const val KEY_COUNT  = "ssh_count"
        private const val KEY_CHOICE = "ssh_choice"
    }

    val name:   StateFlow<String>  = savedStateHandle.getStateFlow(KEY_NAME,   "")
    val count:  StateFlow<Int>     = savedStateHandle.getStateFlow(KEY_COUNT,   0)
    val choice: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_CHOICE,  false)

    init {
        Log.d(TAG, "[SavedStateHandle] ViewModel created: instance=#${hashCode()}")
        // *** BẰNG CHỨNG KEY: Nếu có data ở đây sau kill process → SSH hoạt động! ***
        Log.d(TAG, "[SavedStateHandle] Restored values: " +
                "name='${name.value}', count=${count.value}, choice=${choice.value}")
    }

    fun updateName(newName: String) {
        savedStateHandle[KEY_NAME] = newName
        Log.d(TAG, "[SavedStateHandle] Name changed: '$newName', instance=#${hashCode()}")
    }

    fun incrementCount() {
        savedStateHandle[KEY_COUNT] = count.value + 1
        Log.d(TAG, "[SavedStateHandle] Count changed: ${count.value}, instance=#${hashCode()}")
    }

    fun resetCount() {
        savedStateHandle[KEY_COUNT] = 0
        Log.d(TAG, "[SavedStateHandle] Count reset: 0, instance=#${hashCode()}")
    }

    fun toggleChoice() {
        savedStateHandle[KEY_CHOICE] = !choice.value
        Log.d(TAG, "[SavedStateHandle] Choice changed: ${choice.value}, instance=#${hashCode()}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "[SavedStateHandle] ViewModel cleared: instance=#${hashCode()}")
    }
}
