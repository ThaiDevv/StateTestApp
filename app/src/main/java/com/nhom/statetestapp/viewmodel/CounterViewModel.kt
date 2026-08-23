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
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 3 (TV3)
 * ============================================================
 * Log "ViewModel CREATED" & "ViewModel CLEARED" + hashCode()
 * là BẰNG CHỨNG quan trọng nhất trong test cases:
 *   - Cùng hashCode → ViewModel còn sống (survive)
 *   - hashCode khác → ViewModel đã bị huỷ & tạo mới
 * ============================================================
 */
private const val TAG = "STATE_TEST"

class CounterViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var count by mutableIntStateOf(0)
        private set

    var choice by mutableStateOf(false)
        private set

    init {
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

    fun resetCount() {
        count = 0
        Log.d(TAG, "[ViewModel] resetCount → 0 (vmHash=#${hashCode()})")
    }

    fun toggleChoice() {
        choice = !choice
        Log.d(TAG, "[ViewModel] toggleChoice → $choice (vmHash=#${hashCode()})")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "[ViewModel] ❌ ViewModel CLEARED/DESTROYED - instance: #${hashCode()}")
    }
}
