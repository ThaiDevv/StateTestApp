package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhom.statetestapp.viewmodel.CounterViewModel

/**
 * ============================================================
 * ViewModelScreen – Demo cơ chế `ViewModel`
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 3 (TV3)
 * ============================================================
 *
 * NHIỆM VỤ CỦA TV3:
 * 1. Implement toàn bộ UI bên dưới (phần TODO)
 * 2. ViewModel đã được tạo sẵn tại: viewmodel/CounterViewModel.kt
 *    → TV3 chỉ cần IMPLEMENT, không cần tạo mới
 * 3. Thêm Logcat logging với TAG = "STATE_TEST"
 * 4. Hiển thị VM hashCode trên UI để chứng minh same/different instance
 * 5. Thực hiện và chụp ảnh 5 test cases
 *
 * HƯỚNG DẪN SỬ DỤNG VIEWMODEL:
 *   val vm: CounterViewModel = viewModel()
 *   // Đọc state: vm.name, vm.count, vm.choice
 *   // Ghi state: vm.updateName("..."), vm.incrementCount(), vm.toggleChoice()
 *
 * ⚠️ LƯU Ý QUAN TRỌNG:
 *   Khi navigate đi rồi quay lại, phải test CẢ HAI tình huống:
 *   - Bấm Back (popBackStack) → ViewModel bị huỷ (log CLEARED)
 *   - Navigate giữ entry → ViewModel còn (hashCode giống nhau)
 *   Và phải giải thích sự khác biệt!
 *
 * XEM CHI TIẾT TẠI: implementation_plan.md – Mục 6
 * ============================================================
 */

private const val TAG = "STATE_TEST"

@Composable
fun ViewModelScreen(vm: CounterViewModel = viewModel()) {

    // =========================================================
    // TODO (TV3): Thêm Log ở đây
    // =========================================================
    // Log.d(TAG, "[ViewModel] Recomposition - name='${vm.name}', count=${vm.count}, " +
    //            "choice=${vm.choice}, vmHash=${vm.hashCode()}")

    // =========================================================
    // TODO (TV3): Implement UI
    // UI giống RememberScreen nhưng:
    //   - Label: "🟠 Cơ chế: ViewModel"
    //   - Hiển thị thêm: "VM instance: #${vm.hashCode()}" (dưới label)
    //   - Màu chủ đạo: Color(0xFFFF9800) – Cam
    //   - Màu card label: Color(0xFFFFF3E0) – Cam nhạt
    //   - Dùng: vm.name, vm.count, vm.choice (đọc trực tiếp)
    //   - Gọi: vm.updateName(), vm.incrementCount(), vm.toggleChoice()
    //
    // Tham khảo code mẫu đầy đủ trong: implementation_plan.md – Mục 6.2
    // =========================================================

    // Placeholder (xoá khi TV3 implement xong)
    PlaceholderScreen(
        emoji = "🟠",
        mechanismName = "ViewModel",
        assignee = "TV3",
        color = androidx.compose.ui.graphics.Color(0xFFFF9800)
    )
}
