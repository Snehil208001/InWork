package com.example.inwork.mainui.adminsettings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.permissionscreen.viewmodel.AdminSettingsEvent
import com.example.inwork.mainui.permissionscreen.viewmodel.AdminSettingsViewModel

@Composable
fun AdminSettingsScreen(
    // The ViewModel is now injected by default
    viewModel: AdminSettingsViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // Light green background
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Grant Permissions",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                item {
                    PermissionCard(
                        title = "LOCATION",
                        description = "For usage of maps.",
                        // Send event to ViewModel on click
                        onGrantClick = { viewModel.onEvent(AdminSettingsEvent.GrantLocationClicked) }
                    )
                }

                item {
                    PermissionCard(
                        title = "STORAGE PERMISSION",
                        description = "For the download of Pdfs.",
                        onGrantClick = { viewModel.onEvent(AdminSettingsEvent.GrantStorageClicked) }
                    )
                }

                item {
                    PermissionCard(
                        title = "NOTIFICATIONS",
                        description = "To Post Notifications",
                        onGrantClick = { viewModel.onEvent(AdminSettingsEvent.GrantNotificationsClicked) }
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text(text = description, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8E6C9)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("GRANT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminSettingsScreenPreview() {
    // The preview now works without needing to pass parameters
    AdminSettingsScreen()
}