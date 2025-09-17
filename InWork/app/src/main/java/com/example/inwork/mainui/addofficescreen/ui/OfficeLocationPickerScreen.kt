package com.example.inwork.mainui.addofficescreen.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun OfficeLocationPickerScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val defaultLocation = LatLng(25.5941, 85.1376)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                getLastKnownLocation(context) { location ->
                    location?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                        selectedLocation = currentLocation
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 15f)
                    }
                    isLoading = false
                }
            } else {
                isLoading = false
            }
        }
    )

    LaunchedEffect(Unit) {
        isLoading = true
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation(context) { location ->
                location?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)
                    selectedLocation = currentLocation
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 15f)
                }
                isLoading = false
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                },
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                selectedLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Selected Location"
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedLocation == null && !isLoading) {
                    Text(
                        text = "Tap on the map to select a location",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedLocation?.let {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("picked_latitude", it.latitude)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("picked_longitude", it.longitude)
                            navController.popBackStack()
                        }
                    },
                    enabled = selectedLocation != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
                ) {
                    Text("Confirm Location", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun getLastKnownLocation(context: android.content.Context, onLocationFetched: (android.location.Location?) -> Unit) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: android.location.Location? ->
                onLocationFetched(location)
            }
            .addOnFailureListener {
                onLocationFetched(null)
            }
    }
}