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
import androidx.core.content.ContextCompat
import com.example.inwork.R

@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            mutableStateOf(true)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasNotificationPermission) {
            Text(text = "Notifications are enabled.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDummySystemNotification(context) }) {
                Text("Show Dummy Notification")
            }
            Spacer(modifier = Modifier.height(16.dp))
            DummyNotificationCard()
        } else {
            Text(text = "Notifications are disabled. Please enable them in settings.")
            Button(onClick = { openAppSettings(context) }) {
                Text("Open Settings")
            }
        }
    }
}

// ✨ CORRECTED FUNCTION ✨
fun showDummySystemNotification(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)

    // On older versions, we don't need to check for permission.
    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    if (!hasPermission) {
        // If permission is missing on Android 13+, do nothing.
        // The UI should guide the user to grant it.
        return
    }

    val builder = NotificationCompat.Builder(context, "DUMMY_CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("It Works! 🎉")
        .setContentText("This is a real system notification.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Show the notification
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