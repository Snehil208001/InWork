package com.example.inwork.mainui.permissionscreen.ui

import androidx.compose.foundation.background
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

@Composable
fun GrantPermissionsScreen(
    onGrantLocation: () -> Unit,
    onGrantBackgroundLocation: () -> Unit,
    onGrantScreentime: () -> Unit,
    onGrantPhone: () -> Unit,
    onGrantNotifications: () -> Unit
)
 {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color(0xFFA5D6A7)) // light green background
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                item {
                    Text(
                        text = "Grant Permissions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Location
                item {
                    PermissionCard(
                        title = "LOCATION",
                        description = "For usage of maps.",
                        onGrantClick = onGrantLocation
                    )
                }

                // Background Location
                item {
                    PermissionCard(
                        title = "BACKGROUND LOCATION",
                        description = "Auto CheckIn/CheckOut",
                        onGrantClick = onGrantBackgroundLocation
                    )
                }

                // Screen Time
                item {
                    PermissionCard(
                        title = "SCREENTIME",
                        description = "Screen time analysis for productivity",
                        onGrantClick = onGrantScreentime
                    )
                }

                // Phone
                item {
                    PermissionCard(
                        title = "PHONE",
                        description = "For contacting us",
                        onGrantClick = onGrantPhone
                    )
                }

                // Notifications
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1) // blue title
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8E6C9)),
                shape = RoundedCornerShape(4.dp), // square-like
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("GRANT", color = Color.Black)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GrantPermissionsScreenPreview() {
    GrantPermissionsScreen(
        onGrantLocation = {},
        onGrantBackgroundLocation = {},
        onGrantScreentime = {},
        onGrantPhone = {},
        onGrantNotifications = {}
    )
}
