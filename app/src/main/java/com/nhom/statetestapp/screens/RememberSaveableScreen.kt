package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * ============================================================
 * RememberSaveableScreen – Demo cơ chế `rememberSaveable`
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 2 (TV2)
 * ============================================================
 *
 * NHIỆM VỤ CỦA TV2:
 * 1. Implement toàn bộ UI bên dưới (phần TODO)
 * 2. Thêm Logcat logging với TAG = "STATE_TEST"
 * 3. Đảm bảo 3 state: name (String), count (Int), choice (Boolean)
 * 4. Dùng `rememberSaveable` cho TẤT CẢ state
 * 5. Thực hiện và chụp ảnh 5 test cases
 *
 * HƯỚNG DẪN STATE:
 *   var name   by rememberSaveable { mutableStateOf("") }
 *   var count  by rememberSaveable { mutableIntStateOf(0) }
 *   var choice by rememberSaveable { mutableStateOf(false) }
 *
 * XEM CHI TIẾT TẠI: implementation_plan.md – Mục 5
 * ============================================================
 */

private const val TAG = "STATE_TEST"

@Composable
fun RememberSaveableScreen() {

    // =========================================================
    // TODO (TV2): Khai báo state dùng rememberSaveable ở đây
    // =========================================================
    // var name   by rememberSaveable { mutableStateOf("") }
    // var count  by rememberSaveable { mutableIntStateOf(0) }
    // var choice by rememberSaveable { mutableStateOf(false) }
    //
    // Log.d(TAG, "[rememberSaveable] Recomposition - name='$name', count=$count, choice=$choice")

    // =========================================================
    // TODO (TV2): Implement UI
    // UI giống RememberScreen nhưng:
    //   - Label: "🟢 Cơ chế: rememberSaveable"
    //   - Màu chủ đạo: Color(0xFF4CAF50) – Xanh lá
    //   - Màu card label: Color(0xFFE8F5E9) – Xanh lá nhạt
    //   - Dùng rememberSaveable thay vì remember
    //
    // Tham khảo code mẫu đầy đủ trong: implementation_plan.md – Mục 5.2
    // =========================================================

    // Placeholder (xoá khi TV2 implement xong)
    PlaceholderScreen(
        emoji = "🟢",
        mechanismName = "rememberSaveable",
        assignee = "TV2",
        color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
    )
}
