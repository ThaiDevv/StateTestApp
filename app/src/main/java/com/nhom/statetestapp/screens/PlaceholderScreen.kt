package com.nhom.statetestapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ============================================================
 * PlaceholderScreen – Màn hình chờ khi thành viên chưa implement
 * Phụ trách: TV5 (internal helper, không phải màn hình demo)
 * ============================================================
 * Composable dùng chung để hiển thị "đang chờ implement"
 * khi TV1-TV5 chưa hoàn thành code của mình.
 * KHÔNG cần chỉnh sửa file này.
 * ============================================================
 */
@Composable
fun PlaceholderScreen(
    emoji: String,
    mechanismName: String,
    assignee: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 72.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = mechanismName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⏳ Đang chờ $assignee implement",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$assignee: Mở file ${mechanismName}Screen.kt\n" +
                           "và xoá PlaceholderScreen() ở cuối file,\n" +
                           "sau đó implement TODO bên trên.",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Xem hướng dẫn chi tiết trong implementation_plan.md",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF),
            textAlign = TextAlign.Center
        )
    }
}
