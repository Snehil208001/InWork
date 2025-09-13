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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.inwork.R
import com.example.inwork.core.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PermissionScreen(
    navController: NavController,
    viewModel: PermissionViewModel = viewModel()
) {
    var showBackgroundLocationDialog by remember { mutableStateOf(false) }

    // This launcher handles the background location permission request.
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { viewModel.onBackgroundPermissionResult() }
    )

    // This launcher handles the foreground location permissions request.
    val foregroundLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->

            if (viewModel.onForegroundPermissionResult(permissions)) {
                showBackgroundLocationDialog = true
            }
        }
    )

    // A LaunchedEffect to listen for navigation events from the ViewModel.
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                NavigationEvent.NavigateToLogin -> {
                    navController.navigate(Screen.Login.route) {

                        popUpTo(Screen.Permission.route) { inclusive = true }
                    }
                }
            }
        }
    }

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
            text = "To use the automatic CheckIn/CheckOut feature of the App, allow Inwork to use your location all the time.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Inwork collects location data for automatic check-in and check-out, even when the app is closed or not in use.",
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
                onClick = { viewModel.onPermissionDenied() },
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
            onDismissRequest = {
                showBackgroundLocationDialog = false
                viewModel.onPermissionDenied()
            },
            title = { Text("Background Location Access") },
            text = { Text("This app collects background location data for geofencing and automatic CheckIn/Out, even when the app is closed. Please select 'Allow all the time' to use this feature.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBackgroundLocationDialog = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        } else {

                            viewModel.onBackgroundPermissionResult()
                        }
                    }
                ) { Text("CONTINUE") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showBackgroundLocationDialog = false
                        viewModel.onPermissionDenied()
                    }
                ) { Text("CANCEL") }
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