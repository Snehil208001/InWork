package com.example.inwork.mainui.permissionscreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar // Assuming this exists

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrantPermissionsScreen(
    onNavigateBack: () -> Unit,
    onGrantLocation: () -> Unit,
    onGrantBackgroundLocation: () -> Unit,
    onGrantScreentime: () -> Unit,
    onGrantPhone: () -> Unit,
    onGrantNotifications: () -> Unit
) {
    Scaffold(
        topBar = {
            InWorkTopAppBar(
                title = "Grant Permissions",
                onNavigationIconClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Grant Permissions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                PermissionCard(
                    title = "LOCATION",
                    description = "For usage of maps.",
                    onGrantClick = onGrantLocation
                )
            }

            item {
                PermissionCard(
                    title = "BACKGROUND LOCATION",
                    description = "Auto CheckIn/CheckOut",
                    onGrantClick = onGrantBackgroundLocation
                )
            }

            item {
                PermissionCard(
                    title = "SCREENTIME",
                    description = "Screen time analysis for productivity",
                    onGrantClick = onGrantScreentime
                )
            }

            item {
                PermissionCard(
                    title = "PHONE",
                    description = "For contacting us",
                    onGrantClick = onGrantPhone
                )
            }

            item {
                PermissionCard(
                    title = "NOTIFICATIONS",
                    description = "To Post Notifications",
                    onGrantClick = onGrantNotifications
                )
            }
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    onGrantClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("GRANT")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GrantPermissionsScreenPreview() {
    GrantPermissionsScreen(
        onNavigateBack = {},
        onGrantLocation = {},
        onGrantBackgroundLocation = {},
        onGrantScreentime = {},
        onGrantPhone = {},
        onGrantNotifications = {}
    )
}