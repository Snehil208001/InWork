package com.example.inwork.mainui.geoscreen.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun AddGeoScreen() {
    val context = LocalContext.current
    var deviceLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // This effect will fetch the last known location
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    deviceLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        deviceLocation?.let { latLng ->
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(latLng, 15f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Your Location",
                    snippet = "You are here"
                )
            }
        } ?: run {
            // Show a loading indicator until the location is fetched
            CircularProgressIndicator()
        }
    }
}