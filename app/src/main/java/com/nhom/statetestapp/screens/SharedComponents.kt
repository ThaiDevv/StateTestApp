package com.nhom.statetestapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ============================================================
 * SharedComponents – Các Composable dùng chung cho 5 màn hình
 * Phụ trách: TV5 (internal helper)
 * ============================================================
 * Chứa: MechanismBadge, StateCard, StateResultCard, InfoNote
 * File này KHÔNG cần chỉnh sửa.
 * ============================================================
 */

/** Badge hiển thị tên cơ chế và mô tả ngắn ở đầu mỗi màn hình */
@Composable
fun MechanismBadge(
    label: String,
    description: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF616161),
                lineHeight = 18.sp
            )
        }
    }
}

/** Card bọc quanh mỗi thành phần state (TextField, Button, Switch) */
@Composable
fun StateCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF757575),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            content()
        }
    }
}

/** Card kết quả tổng hợp hiển thị tất cả state hiện tại */
@Composable
fun StateResultCard(
    name: String,
    count: Int,
    choice: Boolean,
    color: Color,
    extra: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📊 STATE HIỆN TẠI",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            ResultRow(label = "Tên",        value = if (name.isEmpty()) "(trống)" else "\"$name\"", color = color)
            ResultRow(label = "Bộ đếm",     value = "$count lần",                                    color = color)
            ResultRow(label = "Lựa chọn",   value = if (choice) "BẬT ✅" else "TẮT ❌",              color = color)

            if (extra != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFF424242))
                Text(
                    text = extra,
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFF9E9E9E))
        Text(
            text  = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontFamily = FontFamily.Monospace
        )
    }
}

/** Note ghi chú hướng dẫn test ở cuối mỗi màn hình */
@Composable
fun InfoNote(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("💡", fontSize = 18.sp)
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color(0xFF5D4037),
                lineHeight = 18.sp
            )
        }
    }
}
