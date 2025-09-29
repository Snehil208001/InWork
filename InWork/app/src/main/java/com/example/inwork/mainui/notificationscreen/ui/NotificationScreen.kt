package com.example.inwork.mainui.notificationscreen.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inwork.R
import com.example.inwork.mainui.notificationscreen.viewmodel.NotificationEvent
import com.example.inwork.mainui.notificationscreen.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Launcher for the notification permission request
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onEvent(NotificationEvent.OnPermissionResult(isGranted))
        }
    )

    // Check for permission when the screen is first composed or resumed
    LaunchedEffect(Unit) {
        viewModel.onEvent(NotificationEvent.CheckInitialPermission(context))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.hasNotificationPermission) {
            // UI when permission is granted
            Text(text = "Notifications are enabled.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDummySystemNotification(context) }) {
                Text("Show Dummy Notification")
            }
            Spacer(modifier = Modifier.height(16.dp))
            DummyNotificationCard()
        } else {
            // UI when permission is denied
            Text(text = "Notifications are disabled.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }) {
                Text("Request Permission")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { openAppSettings(context) }) {
                Text("Open Settings")
            }
        }
    }
}

fun showDummySystemNotification(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)

    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    if (!hasPermission) {
        return
    }

    val builder = NotificationCompat.Builder(context, "DUMMY_CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("It Works! 🎉")
        .setContentText("This is a real system notification.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notificationManager.notify(1, builder.build())
}

@Composable
fun DummyNotificationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "In-App Notification Card",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "This is a UI component, not a system notification.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}