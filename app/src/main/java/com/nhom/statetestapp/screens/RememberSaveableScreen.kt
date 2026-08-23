package com.nhom.statetestapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ============================================================
 * RememberSaveableScreen – Demo cơ chế `rememberSaveable`
 * 👤 THÀNH VIÊN 2 (TV2)
 * ============================================================
 * rememberSaveable lưu state trong Composition (như remember)
 * + tự động backup vào SavedInstanceState Bundle.
 *
 * Survive: recomposition, xoay màn hình, tạo lại Activity, kill process (nếu bấm Home trước).
 * Không survive: navigate Back (NavBackStackEntry bị pop).
 * ============================================================
 */
private const val TAG = "STATE_TEST"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberSaveableScreen() {

    // ===== STATE – dùng `rememberSaveable` =====
    var name   by rememberSaveable { mutableStateOf("") }
    var count  by rememberSaveable { mutableIntStateOf(0) }
    var choice by rememberSaveable { mutableStateOf(false) }

    Log.d(TAG, "[rememberSaveable] ♻️ Recomposition → name='$name', count=$count, choice=$choice")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🟢 rememberSaveable",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F8E9))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            MechanismBadge(
                label = "Cơ chế: rememberSaveable",
                description = "Lưu trong Composition + backup vào Bundle (onSaveInstanceState). " +
                              "Survive xoay màn hình & kill process. Mất khi navigate Back.",
                color = Color(0xFF2E7D32)
            )

            StateCard(title = "📝 Ô nhập tên / ghi chú") {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        Log.d(TAG, "[rememberSaveable] name thay đổi → '$it'")
                    },
                    label = { Text("Nhập tên hoặc ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2E7D32),
                        focusedLabelColor  = Color(0xFF2E7D32)
                    )
                )
            }

            StateCard(title = "🔢 Bộ đếm số lần bấm") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            count++
                            Log.d(TAG, "[rememberSaveable] count tăng → $count")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("＋ Bấm +1", fontSize = 16.sp) }

                    Button(
                        onClick = {
                            count = 0
                            Log.d(TAG, "[rememberSaveable] count reset → 0")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Reset") }

                    Text(
                        text = "$count",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            StateCard(title = "🔘 Lựa chọn On/Off") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Switch(
                        checked = choice,
                        onCheckedChange = {
                            choice = it
                            Log.d(TAG, "[rememberSaveable] choice → $it")
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF2E7D32)
                        )
                    )
                    Text(
                        text = if (choice) "BẬT ✅" else "TẮT ❌",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (choice) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
                    )
                }
            }

            StateResultCard(
                name   = name,
                count  = count,
                choice = choice,
                color  = Color(0xFF2E7D32),
                extra  = null
            )

            InfoNote(
                text = "✅ Thử xoay màn hình (Ctrl+F11) → State VẪN CÒN (khác remember!).\n" +
                       "❌ Thử bấm Back rồi vào lại → State MẤT (NavBackStackEntry bị pop)."
            )
        }
    }
}
