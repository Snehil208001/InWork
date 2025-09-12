package com.example.inwork.mainui.permissionscreen.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.inwork.R
import com.example.inwork.core.navigation.Routes

@Composable
fun PermissionScreen(navController: NavController) {

    val context = LocalContext.current
    var showBackgroundLocationDialog by remember { mutableStateOf(false) }

    // This launcher handles the result from the background location permission request.
    // It will navigate to onboarding whether permission is granted or not.
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            // After the user makes a choice, navigate to the onboarding screen.
            navController.navigate(Routes.onboarding) {
                popUpTo(Routes.permission) { inclusive = true }
            }
        }
    )

    // This launcher handles the result for the initial foreground location permissions.
    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                // If foreground is granted, show the dialog to explain background permission.
                showBackgroundLocationDialog = true
            } else {
                // If the user denies foreground, navigate to onboarding.
                navController.navigate(Routes.onboarding) {
                    popUpTo(Routes.permission) { inclusive = true }
                }
            }
        }
    )

    // --- THE UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoaderAnimation(
            modifier = Modifier.size(250.dp),
            anim = R.raw.earth2,
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Use your Location",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To use automatic CheckIn/CheckOut feature of the App, allow Inwork to use your location all the time.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Inwork collects location data to get automatic checkIn and CheckOut when the Employee enters or exits the office even when the app is closed or not in use.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    // **DENY** button now navigates to onboarding
                    navController.navigate(Routes.onboarding) {
                        popUpTo(Routes.permission) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2DB561),
                    contentColor = Color.White
                )
            ) { Text(text = "DENY", fontSize = 16.sp) }
            Button(
                onClick = {
                    val permissionsToRequest = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    foregroundLocationLauncher.launch(permissionsToRequest)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2DB561),
                    contentColor = Color.White
                )
            ) { Text(text = "ACCEPT", fontSize = 16.sp) }
        }
    }

    // --- BACKGROUND PERMISSION DIALOG ---
    if (showBackgroundLocationDialog) {
        AlertDialog(
            onDismissRequest = { showBackgroundLocationDialog = false },
            title = { Text("Background Location Access") },
            text = { Text("This app collects background location data to enable geofencing for automatic CheckIn/Out even when the app is closed or not in use. Hence ALLOW ALL THE TIME to use this feature.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBackgroundLocationDialog = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        } else {
                            navController.navigate(Routes.onboarding) { popUpTo(0) }
                        }
                    }
                ) {
                    Text("CONTINUE")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showBackgroundLocationDialog = false
                        // Also navigate if user cancels the dialog.
                        navController.navigate(Routes.onboarding) {
                            popUpTo(Routes.permission) { inclusive = true }
                        }
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(anim))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview() {
    PermissionScreen(navController = rememberNavController())
}