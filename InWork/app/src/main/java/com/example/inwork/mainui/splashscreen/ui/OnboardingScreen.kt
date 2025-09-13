package com.example.inwork.mainui.splashscreen.ui // Or your correct package

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(navController: NavController) {

    // Creates a launcher for the permission request.
    // `rememberLauncherForActivityResult` is a Composable function that allows you to launch an activity for a result.
    // `ActivityResultContracts.RequestPermission()` is a predefined contract for requesting a single permission.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            // This is the callback that gets executed when the user responds to the permission dialog.
            if (isGranted) {
                // Permission was granted. You can now send notifications.
                // You might want to navigate to the main part of your app here.
            } else {
                // Permission was denied. You should handle this gracefully.
                // For example, you could show a message explaining why the permission is important.
            }
        }
    )

    // `LaunchedEffect` is used to run a suspend function when the composable is first created.
    // The key `true` ensures that this effect runs only once.
    LaunchedEffect(key1 = true) {
        // The `POST_NOTIFICATIONS` permission is only required for Android 13 (API level 33) and higher.
        // This check ensures that the app doesn't try to request the permission on older versions of Android.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Launch the permission request. This will show the system's permission dialog to the user.
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // This is the UI for the onboarding screen.
    // It's a simple column with a text element.
    Column(
        modifier = Modifier.fillMaxSize(), // The column will take up the entire screen.
        verticalArrangement = Arrangement.Center, // Center the content vertically.
        horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally.
    ) {
        Text(text = "Notifications are important!")
        // You can add more UI elements here, like an explanation of why notifications are needed,
        // or a button to proceed to the next screen.
    }
}