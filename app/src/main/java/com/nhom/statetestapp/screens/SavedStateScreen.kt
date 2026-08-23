package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhom.statetestapp.viewmodel.SavedStateViewModel

/**
 * ============================================================
 * SavedStateScreen – Demo cơ chế `SavedStateHandle`
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 4 (TV4)
 * ============================================================
 *
 * NHIỆM VỤ CỦA TV4:
 * 1. Implement toàn bộ UI bên dưới (phần TODO)
 * 2. ViewModel đã được tạo sẵn tại: viewmodel/SavedStateViewModel.kt
 *    → TV4 chỉ cần IMPLEMENT, không cần tạo mới
 * 3. Collect StateFlow từ ViewModel dùng collectAsStateWithLifecycle()
 * 4. Hiển thị VM hashCode trên UI
 * 5. Thực hiện và chụp ảnh 5 test cases
 *
 * HƯỚNG DẪN SỬ DỤNG:
 *   val vm: SavedStateViewModel = viewModel()
 *   val name  by vm.name.collectAsStateWithLifecycle()
 *   val count by vm.count.collectAsStateWithLifecycle()
 *   val choice by vm.choice.collectAsStateWithLifecycle()
 *   // Ghi: vm.updateName(), vm.incrementCount(), vm.toggleChoice()
 *
 * ⭐ TEST CASE QUAN TRỌNG NHẤT:
 *   Test Case #20: Kill process → state vẫn CÒN
 *   Đây là điểm khác biệt lớn nhất với ViewModel thuần!
 *   Phải giải thích rõ trong báo cáo.
 *
 * XEM CHI TIẾT TẠI: implementation_plan.md – Mục 7
 * ============================================================
 */

private const val TAG = "STATE_TEST"

@Composable
fun SavedStateScreen(vm: SavedStateViewModel = viewModel()) {

    // =========================================================
    // TODO (TV4): Collect StateFlow thành State ở đây
    // =========================================================
    // val name   by vm.name.collectAsStateWithLifecycle()
    // val count  by vm.count.collectAsStateWithLifecycle()
    // val choice by vm.choice.collectAsStateWithLifecycle()
    //
    // Log.d(TAG, "[SavedStateHandle] Recomposition - name='$name', count=$count, " +
    //            "choice=$choice, vmHash=${vm.hashCode()}")

    // =========================================================
    // TODO (TV4): Implement UI
    // UI giống ViewModelScreen nhưng:
    //   - Label: "🔴 Cơ chế: SavedStateHandle"
    //   - Màu chủ đạo: Color(0xFFE91E63) – Hồng/Đỏ
    //   - Màu card label: Color(0xFFFCE4EC) – Hồng nhạt
    //   - Dùng name, count, choice từ collectAsStateWithLifecycle()
    //   - Gọi: vm.updateName(), vm.incrementCount(), vm.toggleChoice()
    //
    // Tham khảo code mẫu đầy đủ trong: implementation_plan.md – Mục 7.2
    // =========================================================

    // Placeholder (xoá khi TV4 implement xong)
    PlaceholderScreen(
        emoji = "🔴",
        mechanismName = "SavedStateHandle",
        assignee = "TV4",
        color = androidx.compose.ui.graphics.Color(0xFFE91E63)
    )
}
