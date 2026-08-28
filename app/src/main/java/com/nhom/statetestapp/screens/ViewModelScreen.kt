package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhom.statetestapp.viewmodel.CounterViewModel

/**
 * ============================================================
 * ViewModelScreen – Demo cơ chế `ViewModel`
 * 👤 THÀNH VIÊN 3 (TV3)
 * ============================================================
 * ViewModel lưu state trong RAM, gắn với ViewModelStoreOwner.
 *
 * Survive: recomposition, xoay màn hình, tạo lại Activity.
 * Không survive: navigate Back (NavBackStackEntry bị pop → onCleared()),
 *                kill process (RAM bị giải phóng).
 *
 * Bằng chứng: Hiển thị VM Hashcode trên UI để so sánh
 *             cùng instance hay instance mới sau các thao tác.
 * ============================================================
 */
private const val TAG = "STATE_TEST"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewModelScreen(vm: CounterViewModel = viewModel()) {

    Log.d(TAG, "[ViewModel] Recomposition: name='${vm.name}', count=${vm.count}, " +
               "choice=${vm.choice}, instance=#${vm.hashCode()}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ViewModel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE65100),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8F0))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            MechanismBadge(
                label = "Cơ chế: ViewModel",
                description = "Dữ liệu được giữ trong ViewModel và không phụ thuộc vào Composition.",
                color = Color(0xFFE65100)
            )

            // ── Hiển thị VM Instance ID – bằng chứng cốt lõi ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Mã ViewModel",
                            fontSize = 12.sp,
                            color = Color(0xFF795548),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "#${vm.hashCode()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                        Text(
                            "Mã thay đổi khi một ViewModel mới được tạo",
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E)
                        )
                }
            }

            StateCard(title = "Tên hoặc ghi chú") {
                OutlinedTextField(
                    value = vm.name,
                    onValueChange = { vm.updateName(it) },
                    label = { Text("Nhập tên hoặc ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE65100),
                        focusedLabelColor  = Color(0xFFE65100)
                    )
                )
            }

            StateCard(title = "Bộ đếm") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { vm.incrementCount() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("+1", fontSize = 16.sp) }

                    Button(
                        onClick = { vm.resetCount() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Reset") }

                    Text(
                        text = "${vm.count}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100)
                    )
                }
            }

            StateCard(title = "Lựa chọn") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Switch(
                        checked = vm.choice,
                        onCheckedChange = { vm.toggleChoice() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFE65100)
                        )
                    )
                    Text(
                        text = if (vm.choice) "Bật" else "Tắt",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (vm.choice) Color(0xFFE65100) else Color(0xFF9E9E9E)
                    )
                }
            }

            StateResultCard(
                name   = vm.name,
                count  = vm.count,
                choice = vm.choice,
                color  = Color(0xFFE65100),
                extra  = "VM Instance: #${vm.hashCode()}"
            )

            InfoNote(
                text = "So sánh mã ViewModel trước và sau khi xoay màn hình, bấm Back hoặc kill process."
            )
        }
    }
}
