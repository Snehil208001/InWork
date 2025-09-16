package com.example.inwork.mainui.geoscreen.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inwork.mainui.geoscreen.viewmodel.AddGeoEvent
import com.example.inwork.mainui.geoscreen.viewmodel.AddGeoViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun AddGeoScreen(
    viewModel: AddGeoViewModel = hiltViewModel() // ✅ Use hiltViewModel for injection
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            viewModel.onEvent(AddGeoEvent.PermissionResult(isGranted))
            if (isGranted) {
                viewModel.onEvent(AddGeoEvent.GetLastKnownLocation(context))
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.onEvent(AddGeoEvent.GetLastKnownLocation(context))
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = state.cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        )

        // ✅ Update UI based on the new state
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        state.errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}