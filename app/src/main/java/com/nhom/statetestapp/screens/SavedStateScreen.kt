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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhom.statetestapp.viewmodel.SavedStateViewModel

/**
 * ============================================================
 * SavedStateScreen – Demo cơ chế `SavedStateHandle`
 * 👤 THÀNH VIÊN 4 (TV4)
 * ============================================================
 * SavedStateHandle = ViewModel + tự động backup vào Bundle của OS.
 *
 * Survive: recomposition, xoay màn hình, tạo lại Activity, kill process.
 * Không survive: navigate Back (NavBackStackEntry bị pop).
 *
 * Điểm khác biệt với ViewModel thuần:
 *   - Sau kill process: VM Hash KHÁC nhưng data VẪN CÒN!
 *   - Log "Restored values" trong init sẽ chứng minh điều này.
 * ============================================================
 */
private const val TAG = "STATE_TEST"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedStateScreen(vm: SavedStateViewModel = viewModel()) {

    // Collect StateFlow → State (tự cập nhật UI khi data thay đổi)
    val name   by vm.name.collectAsStateWithLifecycle()
    val count  by vm.count.collectAsStateWithLifecycle()
    val choice by vm.choice.collectAsStateWithLifecycle()

    Log.d(TAG, "[SavedStateHandle] Recomposition: name='$name', count=$count, " +
               "choice=$choice, instance=#${vm.hashCode()}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SavedStateHandle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFC62828),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF0F0))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            MechanismBadge(
                label = "Cơ chế: SavedStateHandle",
                description = "Dữ liệu trong ViewModel được lưu thêm bằng SavedStateHandle.",
                color = Color(0xFFC62828)
            )

            // ── VM Instance ID ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            "Mã ViewModel",
                            fontSize = 12.sp,
                            color = Color(0xFF880E4F),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "#${vm.hashCode()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC62828),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            "So sánh mã và dữ liệu sau khi process được tạo lại",
                            fontSize = 11.sp,
                            color = Color(0xFFC62828)
                        )
                }
            }

            StateCard(title = "Tên hoặc ghi chú") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { vm.updateName(it) },
                    label = { Text("Nhập tên hoặc ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFC62828),
                        focusedLabelColor  = Color(0xFFC62828)
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("+1", fontSize = 16.sp) }

                    Button(
                        onClick = { vm.resetCount() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Reset") }

                    Text(
                        text = "$count",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFC62828)
                    )
                }
            }

            StateCard(title = "Lựa chọn") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Switch(
                        checked = choice,
                        onCheckedChange = { vm.toggleChoice() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFC62828)
                        )
                    )
                    Text(
                        text = if (choice) "Bật" else "Tắt",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (choice) Color(0xFFC62828) else Color(0xFF9E9E9E)
                    )
                }
            }

            StateResultCard(
                name   = name,
                count  = count,
                choice = choice,
                color  = Color(0xFFC62828),
                extra  = "VM Instance: #${vm.hashCode()}"
            )

            InfoNote(
                text = "Sau khi kill process, so sánh mã ViewModel và kiểm tra dòng " +
                       "'Restored values' trong Logcat."
            )
        }
    }
}
