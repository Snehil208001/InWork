package com.example.inwork.mainui.permissionscreen.ui

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.permissionscreen.viewmodel.GrantPermissionsViewModel

// Helper function to check for the Usage Access (Screen Time) permission
private fun isUsageStatsPermissionGranted(context: Context): Boolean {
    // This permission only exists on API 21+
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
        return false
    }
    val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOpsManager.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}


@Composable
fun GrantPermissionsScreen(
    viewModel: GrantPermissionsViewModel = viewModel()
) {
    val context = LocalContext.current

    // --- CHANGE: Check initial permission status ---
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasBackgroundLocationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Not applicable on older versions
            }
        )
    }

    var hasScreenTimePermission by remember {
        mutableStateOf(isUsageStatsPermissionGranted(context))
    }

    var hasPhonePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasNotificationsPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Granted by default on older versions
            }
        )
    }


    // --- Permission Launchers ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasBackgroundLocationPermission = isGranted }
    )

    val phonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasPhonePermission = isGranted }
    )

    val notificationsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasNotificationsPermission = isGranted }
    )

    // --- CHANGE: Re-check screen time permission when returning from settings ---
    val screenTimePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // When the user returns from the settings screen, update the status.
        hasScreenTimePermission = isUsageStatsPermissionGranted(context)
    }

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
                item {
                    PermissionCard(
                        title = "LOCATION",
                        description = "For usage of maps.",
                        // --- CHANGE: Pass the granted status ---
                        isGranted = hasLocationPermission,
                        onGrantClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                    )
                }
                item {
                    PermissionCard(
                        title = "BACKGROUND LOCATION",
                        description = "Auto CheckIn/CheckOut",
                        // --- CHANGE: Pass the granted status ---
                        isGranted = hasBackgroundLocationPermission,
                        onGrantClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            }
                        }
                    )
                }
                item {
                    PermissionCard(
                        title = "SCREENTIME",
                        description = "Screen time analysis for productivity",
                        // --- CHANGE: Pass the granted status ---
                        isGranted = hasScreenTimePermission,
                        onGrantClick = {
                            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            val uri = Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            screenTimePermissionLauncher.launch(intent)
                        }
                    )
                }
                item {
                    PermissionCard(
                        title = "PHONE",
                        description = "For contacting us",
                        // --- CHANGE: Pass the granted status ---
                        isGranted = hasPhonePermission,
                        onGrantClick = { phonePermissionLauncher.launch(Manifest.permission.CALL_PHONE) }
                    )
                }
                // --- CHANGE: Conditionally show Notification permission ---
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    item {
                        PermissionCard(
                            title = "NOTIFICATIONS",
                            description = "To Post Notifications",
                            // --- CHANGE: Pass the granted status ---
                            isGranted = hasNotificationsPermission,
                            onGrantClick = {
                                notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    // --- CHANGE: Added isGranted parameter ---
    isGranted: Boolean,
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
                // --- CHANGE: Update button state and color based on isGranted ---
                enabled = !isGranted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGranted) Color.Gray else Color(0xFFC8E6C9),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                // --- CHANGE: Update button text based on isGranted ---
                Text(
                    text = if (isGranted) "GRANTED" else "GRANT",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GrantPermissionsScreenPreview() {
    GrantPermissionsScreen()
}