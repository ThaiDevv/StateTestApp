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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom.statetestapp.data.DataStoreManager
import com.nhom.statetestapp.data.dataStore
import kotlinx.coroutines.launch

/**
 * ============================================================
 * DataStoreScreen – Demo cơ chế `DataStore`
 * 👤 THÀNH VIÊN 5 (TV5)
 * ============================================================
 * DataStore lưu dữ liệu vào file vật lý trên Internal Storage.
 *
 * Survive: TẤT CẢ tình huống! Recomposition, xoay màn hình,
 *          navigate Back, tạo lại Activity, kill process, thậm chí tắt máy.
 * Không mất khi: Chỉ mất khi user Clear Data hoặc Uninstall app.
 *
 * Đây là cơ chế duy nhất survive navigate Back!
 * ============================================================
 */
private const val TAG = "STATE_TEST"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStoreScreen() {
    val context    = LocalContext.current
    val dsManager  = remember { DataStoreManager(context.dataStore) }
    val scope      = rememberCoroutineScope()

    // Collect Flow → State (reactive, tự động cập nhật UI khi file thay đổi)
    val name   by dsManager.nameFlow.collectAsState(initial = "")
    val count  by dsManager.countFlow.collectAsState(initial = 0)
    val choice by dsManager.choiceFlow.collectAsState(initial = false)

    Log.d(TAG, "[DataStore] ♻️ Recomposition → name='$name', count=$count, choice=$choice")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🟣 DataStore",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A148C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F0FF))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            MechanismBadge(
                label = "Cơ chế: DataStore (Preferences)",
                description = "Lưu vào FILE trên Internal Storage (Disk). Survive TẤT CẢ tình huống " +
                              "kể cả navigate Back và kill process!",
                color = Color(0xFF4A148C)
            )

            // ── Disk badge ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("💾", fontSize = 28.sp)
                    Column {
                        Text(
                            "Lưu trữ trên Disk",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A148C)
                        )
                        Text(
                            "File: data/data/com.nhom.statetestapp/files/datastore/state_test_preferences.preferences_pb",
                            fontSize = 10.sp,
                            color = Color(0xFF7B1FA2),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }

            StateCard(title = "📝 Ô nhập tên / ghi chú") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { newName ->
                        scope.launch { dsManager.saveName(newName) }
                    },
                    label = { Text("Nhập tên hoặc ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A148C),
                        focusedLabelColor  = Color(0xFF4A148C)
                    )
                )
            }

            StateCard(title = "🔢 Bộ đếm số lần bấm") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { scope.launch { dsManager.saveCount(count + 1) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("＋ Bấm +1", fontSize = 16.sp) }

                    Button(
                        onClick = { scope.launch { dsManager.saveCount(0) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78909C)),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("Reset") }

                    Text(
                        text = "$count",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4A148C)
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
                        onCheckedChange = { newChoice ->
                            scope.launch { dsManager.saveChoice(newChoice) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4A148C)
                        )
                    )
                    Text(
                        text = if (choice) "BẬT ✅" else "TẮT ❌",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (choice) Color(0xFF4A148C) else Color(0xFF9E9E9E)
                    )
                }
            }

            StateResultCard(
                name   = name,
                count  = count,
                choice = choice,
                color  = Color(0xFF4A148C),
                extra  = "💾 Dữ liệu đã được ghi xuống Disk"
            )

            InfoNote(
                text = "✅ Bấm Back rồi vào lại → State VẪN CÒN (Khác hoàn toàn 4 cơ chế trên!).\n" +
                       "✅ Kill process → State VẪN CÒN.\n" +
                       "Xem Logcat: '[DataStore] 📖 Read ... from disk' để chứng minh."
            )
        }
    }
}
