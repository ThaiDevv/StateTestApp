package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.runtime.*

/**
 * ============================================================
 * RememberScreen – Demo cơ chế `remember`
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 1 (TV1)
 * ============================================================
 *
 * NHIỆM VỤ CỦA TV1:
 * 1. Implement toàn bộ UI bên dưới (phần TODO)
 * 2. Thêm Logcat logging với TAG = "STATE_TEST"
 * 3. Đảm bảo 3 state: name (String), count (Int), choice (Boolean)
 * 4. Dùng `remember` cho TẤT CẢ state
 * 5. Thực hiện và chụp ảnh 5 test cases
 *
 * HƯỚNG DẪN STATE:
 *   var name   by remember { mutableStateOf("") }
 *   var count  by remember { mutableIntStateOf(0) }
 *   var choice by remember { mutableStateOf(false) }
 *
 * XEM CHI TIẾT TẠI: implementation_plan.md – Mục 4
 * ============================================================
 */

private const val TAG = "STATE_TEST"

@Composable
fun RememberScreen() {

    // =========================================================
    // TODO (TV1): Khai báo state dùng remember ở đây
    // =========================================================
    // var name   by remember { mutableStateOf("") }
    // var count  by remember { mutableIntStateOf(0) }
    // var choice by remember { mutableStateOf(false) }
    //
    // Log.d(TAG, "[remember] Recomposition - name='$name', count=$count, choice=$choice")

    // =========================================================
    // TODO (TV1): Implement UI bên dưới
    // UI gồm:
    //   1. Card hiển thị label "🔵 Cơ chế: remember"
    //   2. OutlinedTextField – nhập name
    //   3. Button "+1" + Text hiển thị count
    //   4. Switch + Text hiển thị choice
    //   5. Card hiển thị state hiện tại (name, count, choice)
    //   6. Nút "Quay lại" (optional)
    //
    // Màu chủ đạo: Color(0xFF2196F3) – Xanh dương
    // Màu card label: Color(0xFFE3F2FD) – Xanh dương nhạt
    //
    // Tham khảo code mẫu đầy đủ trong: implementation_plan.md – Mục 4.2
    // =========================================================

    // Placeholder (xoá khi TV1 implement xong)
    PlaceholderScreen(
        emoji = "🔵",
        mechanismName = "remember",
        assignee = "TV1",
        color = androidx.compose.ui.graphics.Color(0xFF2196F3)
    )
}
