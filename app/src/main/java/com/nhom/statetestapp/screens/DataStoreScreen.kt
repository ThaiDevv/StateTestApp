package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.nhom.statetestapp.data.DataStoreManager
import com.nhom.statetestapp.data.dataStore
import kotlinx.coroutines.launch

/**
 * ============================================================
 * DataStoreScreen – Demo cơ chế `DataStore`
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 5 (TV5)
 * ============================================================
 *
 * NHIỆM VỤ CỦA TV5:
 * 1. Implement toàn bộ UI bên dưới (phần TODO)
 * 2. DataStoreManager đã được tạo sẵn tại: data/DataStoreManager.kt
 * 3. Collect Flow thành State dùng collectAsState()
 * 4. Ghi dữ liệu phải dùng coroutine scope (rememberCoroutineScope)
 * 5. Thực hiện và chụp ảnh 5 test cases
 *
 * HƯỚNG DẪN SỬ DỤNG:
 *   val context = LocalContext.current
 *   val dsManager = remember { DataStoreManager(context.dataStore) }
 *   val scope = rememberCoroutineScope()
 *
 *   val name  by dsManager.nameFlow.collectAsState(initial = "")
 *   val count by dsManager.countFlow.collectAsState(initial = 0)
 *   val choice by dsManager.choiceFlow.collectAsState(initial = false)
 *
 *   // Để ghi:
 *   scope.launch { dsManager.saveName("...") }
 *
 * ⭐ ĐẶC ĐIỂM CỦA DATASTORE:
 *   - Lưu trên DISK → survive MỌI tình huống kể cả kill process
 *   - Cần giải thích: DataStore KHÔNG liên quan đến Composition,
 *     Activity lifecycle, hay back stack
 *
 * XEM CHI TIẾT TẠI: implementation_plan.md – Mục 8
 * ============================================================
 */

private const val TAG = "STATE_TEST"

@Composable
fun DataStoreScreen() {

    // =========================================================
    // TODO (TV5): Setup DataStoreManager và coroutine scope
    // =========================================================
    // val context = LocalContext.current
    // val dsManager = remember { DataStoreManager(context.dataStore) }
    // val scope = rememberCoroutineScope()

    // =========================================================
    // TODO (TV5): Collect Flow thành State
    // =========================================================
    // val name   by dsManager.nameFlow.collectAsState(initial = "")
    // val count  by dsManager.countFlow.collectAsState(initial = 0)
    // val choice by dsManager.choiceFlow.collectAsState(initial = false)
    //
    // Log.d(TAG, "[DataStore] Recomposition - name='$name', count=$count, choice=$choice")

    // =========================================================
    // TODO (TV5): Implement UI
    // UI giống các screen khác nhưng:
    //   - Label: "🟣 Cơ chế: DataStore"
    //   - Màu chủ đạo: Color(0xFF9C27B0) – Tím
    //   - Màu card label: Color(0xFFEDE7F6) – Tím nhạt
    //   - Khi ghi state: scope.launch { dsManager.saveXxx(...) }
    //   - Thêm note: "💾 Dữ liệu lưu trên disk (persist vĩnh viễn)"
    //
    // Tham khảo code mẫu đầy đủ trong: implementation_plan.md – Mục 8.2
    // =========================================================

    // Placeholder (xoá khi TV5 implement xong)
    PlaceholderScreen(
        emoji = "🟣",
        mechanismName = "DataStore",
        assignee = "TV5",
        color = androidx.compose.ui.graphics.Color(0xFF9C27B0)
    )
}
