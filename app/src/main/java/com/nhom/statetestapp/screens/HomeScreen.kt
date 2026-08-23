package com.nhom.statetestapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * ============================================================
 * HomeScreen – Màn hình chính chọn cơ chế để test
 * Phụ trách: TV5
 * ============================================================
 * KHÔNG cần chỉnh sửa. TV5 đã setup sẵn.
 * ============================================================
 */
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Text(
            text = "🔍",
            fontSize = 56.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "State Android\ntồn tại được bao lâu?",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Chọn cơ chế muốn kiểm thử",
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 5 nút điều hướng
        MechanismButton(
            emoji = "🔵",
            title = "remember",
            subtitle = "TV1 phụ trách • Tồn tại trong Composition",
            color = Color(0xFF2196F3),
            onClick = { navController.navigate("remember") }
        )

        MechanismButton(
            emoji = "🟢",
            title = "rememberSaveable",
            subtitle = "TV2 phụ trách • Survive configuration change",
            color = Color(0xFF4CAF50),
            onClick = { navController.navigate("rememberSaveable") }
        )

        MechanismButton(
            emoji = "🟠",
            title = "ViewModel",
            subtitle = "TV3 phụ trách • Survive xoay màn hình",
            color = Color(0xFFFF9800),
            onClick = { navController.navigate("viewModel") }
        )

        MechanismButton(
            emoji = "🔴",
            title = "SavedStateHandle",
            subtitle = "TV4 phụ trách • Survive process death",
            color = Color(0xFFE91E63),
            onClick = { navController.navigate("savedState") }
        )

        MechanismButton(
            emoji = "🟣",
            title = "DataStore",
            subtitle = "TV5 phụ trách • Persist vĩnh viễn trên disk",
            color = Color(0xFF9C27B0),
            onClick = { navController.navigate("dataStore") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Footer note
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "📋 Mỗi màn hình có: TextField nhập tên • Bộ đếm • Switch lựa chọn\n" +
                       "Nhập dữ liệu rồi thực hiện các thao tác test để quan sát state.",
                modifier = Modifier.padding(16.dp),
                fontSize = 13.sp,
                color = Color(0xFF6B5900),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun MechanismButton(
    emoji: String,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = color
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 16.sp
                )
            }
            Text("›", fontSize = 22.sp, color = Color(0xFFD1D5DB))
        }
    }
}
