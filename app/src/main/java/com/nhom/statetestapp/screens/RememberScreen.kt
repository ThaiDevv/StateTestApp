package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ============================================================
 * RememberScreen – Demo cơ chế `remember`
 * 👤 THÀNH VIÊN 1 (TV1)
 * ============================================================
 * remember lưu state trong Composition (RAM).
 * Bị MẤT khi: xoay màn hình, navigate back, kill process.
 * Còn khi: gây recomposition (vẫn trong cùng Composition).
 * ============================================================
 */
private const val TAG = "STATE_TEST"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberScreen() {

    // ===== STATE – dùng `remember` =====
    var name   by remember { mutableStateOf("") }
    var count  by remember { mutableIntStateOf(0) }
    var choice by remember { mutableStateOf(false) }

    // Log mỗi lần Recomposition – bằng chứng state còn hay mất
    Log.d(TAG, "[remember] Recomposition: name='$name', count=$count, choice=$choice")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "remember",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F4FF))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Chip nhận dạng cơ chế ──
            MechanismBadge(
                label = "Cơ chế: remember",
                description = "Dữ liệu được lưu trong Composition và chỉ còn khi Composition vẫn tồn tại.",
                color = Color(0xFF1565C0)
            )

            // ── TextField nhập tên ──
            StateCard(title = "Tên hoặc ghi chú") {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        Log.d(TAG, "[remember] Name changed: '$it'")
                    },
                    label = { Text("Nhập tên hoặc ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        focusedLabelColor  = Color(0xFF1565C0)
                    )
                )
            }

            // ── Bộ đếm ──
            StateCard(title = "Bộ đếm") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            count++
                            Log.d(TAG, "[remember] Count changed: $count")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("+1", fontSize = 16.sp) }

                    Button(
                        onClick = {
                            count = 0
                            Log.d(TAG, "[remember] Count reset: 0")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Reset") }

                    Text(
                        text = "$count",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            // ── Switch lựa chọn ──
            StateCard(title = "Lựa chọn") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Switch(
                        checked = choice,
                        onCheckedChange = {
                            choice = it
                            Log.d(TAG, "[remember] Choice changed: $it")
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor  = Color.White,
                            checkedTrackColor  = Color(0xFF1565C0)
                        )
                    )
                    Text(
                        text = if (choice) "Bật" else "Tắt",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (choice) Color(0xFF1565C0) else Color(0xFF9E9E9E)
                    )
                }
            }

            // ── Bảng state hiện tại ──
            StateResultCard(
                name   = name,
                count  = count,
                choice = choice,
                color  = Color(0xFF1565C0),
                extra  = null
            )

            // ── Ghi chú ──
            InfoNote(
                text = "Xoay màn hình hoặc bấm Back rồi vào lại để kiểm tra state. " +
                       "Nhập thêm một ký tự để tạo recomposition."
            )
        }
    }
}
