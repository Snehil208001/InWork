package com.example.inwork.mainui.geoscreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Represents the state of the AddGeoScreen
data class AddGeoState(
    val currentLocation: Location? = null,
    val cameraPositionState: CameraPositionState = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(LatLng(25.5941, 85.1376), 10f)
    )
)

// Represents the events that can be triggered from the UI
sealed class AddGeoEvent {
    data class PermissionResult(val isGranted: Boolean) : AddGeoEvent()
    data class GetLastKnownLocation(val context: Context) : AddGeoEvent()
}

class AddGeoViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddGeoState())
    val state: StateFlow<AddGeoState> = _state.asStateFlow()

    fun onEvent(event: AddGeoEvent) {
        when (event) {
            is AddGeoEvent.PermissionResult -> {
                if (event.isGranted) {
                    // Permission is granted, you can now get the location.
                    // However, we will get the location on launch of the screen.
                }
            }
            is AddGeoEvent.GetLastKnownLocation -> {
                getLastKnownLocation(event.context)
            }
        }
    }

    private fun getLastKnownLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val newLatLng = LatLng(it.latitude, it.longitude)
                        _state.update { currentState ->
                            currentState.copy(
                                currentLocation = it,
                                cameraPositionState = CameraPositionState(
                                    position = CameraPosition.fromLatLngZoom(newLatLng, 15f)
                                )
                            )
                        }
                    }
                }
        }
    }
}