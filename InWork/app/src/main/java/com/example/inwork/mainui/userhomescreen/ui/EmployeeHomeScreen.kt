package com.example.inwork.mainui.userhomescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.inwork.ui.theme.LightGreen

@Composable
fun EmployeeHomeScreen(
    currentLatitude: Double?,
    currentLongitude: Double?,
    currentAddress: String,
    onShowOnMapClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val latitudeText = currentLatitude?.let { String.format("%.7f", it) } ?: "N/A"
    val longitudeText = currentLongitude?.let { String.format("%.7f", it) } ?: "N/A"
    val locationDisplay = "Latitude: $latitudeText Longitude: $longitudeText"

    // --- The scrollable Column ---
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight() // Explicitly take up max height of the parent
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enables scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Location Card (Color Changed to White) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            shape = RoundedCornerShape(12.dp),
            // FIX: Changed containerColor to Color.White
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = locationDisplay,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Address: $currentAddress",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Update Location Section, wired to onRefreshClick
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onRefreshClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Update Location", fontSize = 14.sp, color = Color.Black)
                    }

                    // ADD GEOFENCE Button and "office location" label
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onShowOnMapClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("ADD GEOFENCE", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                        Text(
                            text = "office location",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                TextButton(
                    onClick = onShowOnMapClick,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, start = 0.dp)
                ) {
                    Text(
                        "Show on the map",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ADD GEOFENCE",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // --- App Usage Details Card (EXPAND/COLLAPSE) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "App Usage Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (isExpanded) "show less>>>" else "show more>>>",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { isExpanded = !isExpanded }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (isExpanded) {
                    AppUsageDetailsContent()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "Total Duration : 03:35:05",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
        // --- End App Usage Details Card ---

        // --- Check In/Out Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { /* onManualCheckIn() */ },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("MANUAL CHECK IN", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { /* onManualCheckOut() */ },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("MANUAL CHECK OUT", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- App Usage Detail Composables ---

@Composable
fun AppUsageDetailsContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        UsageDetailRow(app = "WhatsApp Duration", duration = "00:03:43")
        UsageDetailRow(app = "Facebook Duration", duration = "00:00:00")
        UsageDetailRow(app = "Instagram Duration", duration = "00:03:27")
        UsageDetailRow(app = "Twitter Duration", duration = "00:00:00")
        UsageDetailRow(app = "Newsstand Duration", duration = "00:00:00")
        UsageDetailRow(app = "Games Duration", duration = "00:00:00")
        UsageDetailRow(app = "Calls Duration", duration = "00:00:00")
        UsageDetailRow(app = "Others Duration", duration = "03:27:54")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { /* Handle ALLOW */ },
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("ALLOW", color = Color.White, fontSize = 14.sp)
        }
        Button(
            onClick = { /* Handle SEND DETAILS */ },
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("SEND DETAILS", color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun UsageDetailRow(app: String, duration: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = app, fontSize = 16.sp, color = Color.Black)
        Text(text = duration, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmployeeHomeScreenPreview() {
    EmployeeHomeScreen(
        currentLatitude = 25.5870856,
        currentLongitude = 85.1347947,
        currentAddress = "H4PM+RWW, Lohia Nagar, Patna, Bihar 800020, India",
        onShowOnMapClick = {},
        onRefreshClick = {}
    )
}