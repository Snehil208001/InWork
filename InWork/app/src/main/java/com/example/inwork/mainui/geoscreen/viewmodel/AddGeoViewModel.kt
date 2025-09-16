package com.example.inwork.mainui.geoscreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Represents the state of the AddGeoScreen
data class AddGeoState(
    val isLoading: Boolean = true, // To show a loading indicator
    val errorMessage: String? = null, // To show error messages
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

// ✅ Add Hilt annotations for proper dependency injection
@HiltViewModel
class AddGeoViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddGeoState())
    val state: StateFlow<AddGeoState> = _state.asStateFlow()

    fun onEvent(event: AddGeoEvent) {
        when (event) {
            is AddGeoEvent.PermissionResult -> {
                if (!event.isGranted) {
                    _state.update { it.copy(errorMessage = "Location permission denied.") }
                }
            }
            is AddGeoEvent.GetLastKnownLocation -> {
                getLastKnownLocation(event.context)
            }
        }
    }

    private fun getLastKnownLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _state.update { it.copy(isLoading = true) }
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val newLatLng = LatLng(location.latitude, location.longitude)
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                currentLocation = location,
                                cameraPositionState = CameraPositionState(
                                    position = CameraPosition.fromLatLngZoom(newLatLng, 15f)
                                )
                            )
                        }
                    } else {
                        // Handle case where location is null (e.g., GPS is off)
                        _state.update { it.copy(isLoading = false, errorMessage = "Could not retrieve location. Please ensure GPS is enabled.") }
                    }
                }
                // ✅ ADD THIS FAILURE LISTENER
                .addOnFailureListener { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to get location: ${e.message}"
                        )
                    }
                    Log.e("AddGeoViewModel", "Location fetch failed", e)
                }
        }
    }
}