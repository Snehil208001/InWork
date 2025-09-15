package com.example.inwork.mainui.userhomescreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.inwork.mainui.userhomescreen.ui.UserScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserHomeState(
    // The navigation history. The last item is the current screen.
    val screenStack: List<UserScreen> = listOf(UserScreen.Home),
    val hasLocationPermission: Boolean = false
) {
    // Helper to easily get the current screen
    val currentScreen: UserScreen
        get() = screenStack.last()
}

sealed class UserHomeEvent {
    data class ScreenSelected(val screen: UserScreen) : UserHomeEvent()
    data class LocationPermissionUpdated(val isGranted: Boolean) : UserHomeEvent()
    data class CheckLocationPermission(val context: Context) : UserHomeEvent()
    object NavigateBack : UserHomeEvent() // Event for back navigation
    // Add other events here, e.g., object SosButtonClicked
}

class UserHomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(UserHomeState())
    val state: StateFlow<UserHomeState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun onEvent(event: UserHomeEvent) {
        when (event) {
            is UserHomeEvent.ScreenSelected -> {
                // When selecting from the bottom bar, it's a primary navigation action,
                // so we reset the stack to that screen.
                if (_state.value.currentScreen != event.screen) {
                    _state.update { it.copy(screenStack = listOf(event.screen)) }
                }
            }
            is UserHomeEvent.NavigateBack -> {
                // If there's more than one screen, remove the top one to go back
                if (_state.value.screenStack.size > 1) {
                    _state.update {
                        val newStack = it.screenStack.toMutableList().apply { removeLast() }
                        it.copy(screenStack = newStack)
                    }
                }
            }
            // --- Location Permission Events ---
            is UserHomeEvent.LocationPermissionUpdated -> {
                _state.update { it.copy(hasLocationPermission = event.isGranted) }
            }
            is UserHomeEvent.CheckLocationPermission -> {
                val isGranted = ContextCompat.checkSelfPermission(
                    event.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                _state.update { it.copy(hasLocationPermission = isGranted) }
            }
        }
    }
}