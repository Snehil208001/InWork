package com.example.inwork.mainui.adminhomescreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.inwork.mainui.adminhomescreen.ui.AdminScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AdminHomeState(
    val screenStack: List<AdminScreen> = listOf(AdminScreen.Home),
    val hasLocationPermission: Boolean = false
) {
    val currentScreen: AdminScreen
        get() = screenStack.last()
}

sealed class AdminHomeEvent {
    data class ScreenSelected(val screen: AdminScreen) : AdminHomeEvent()
    data class LocationPermissionUpdated(val isGranted: Boolean) : AdminHomeEvent()
    data class CheckLocationPermission(val context: Context) : AdminHomeEvent()
    object CreateNoticeClicked : AdminHomeEvent()
    object AddEventClicked : AdminHomeEvent()
    object AddEmployeeClicked : AdminHomeEvent()
    object AddOfficesClicked : AdminHomeEvent()
    object NavigateBack : AdminHomeEvent()
}

class AdminHomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(AdminHomeState())
    val state: StateFlow<AdminHomeState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun onEvent(event: AdminHomeEvent) {
        when (event) {
            is AdminHomeEvent.ScreenSelected -> {
                // THIS IS THE FIX
                // If the bottom bar is used to select Home, reset the stack.
                // Otherwise, push the new screen onto the existing stack.
                if (event.screen == AdminScreen.Home) {
                    _state.update { it.copy(screenStack = listOf(AdminScreen.Home)) }
                } else if (_state.value.currentScreen != event.screen) {
                    _state.update {
                        val newStack = it.screenStack.toMutableList().apply { add(event.screen) }
                        it.copy(screenStack = newStack)
                    }
                }
            }
            is AdminHomeEvent.CreateNoticeClicked -> {
                _state.update {
                    val newStack = it.screenStack.toMutableList().apply { add(AdminScreen.CreateNotice) }
                    it.copy(screenStack = newStack)
                }
            }
            is AdminHomeEvent.AddEventClicked -> {
                _state.update {
                    val newStack = it.screenStack.toMutableList().apply { add(AdminScreen.AddEvent) }
                    it.copy(screenStack = newStack)
                }
            }
            is AdminHomeEvent.AddEmployeeClicked -> {
                _state.update {
                    val newStack = it.screenStack.toMutableList().apply { add(AdminScreen.AddEmployee) }
                    it.copy(screenStack = newStack)
                }
            }
            is AdminHomeEvent.AddOfficesClicked -> {
                _state.update {
                    val newStack = it.screenStack.toMutableList().apply { add(AdminScreen.AddOffice) }
                    it.copy(screenStack = newStack)
                }
            }
            is AdminHomeEvent.NavigateBack -> {
                if (_state.value.screenStack.size > 1) {
                    _state.update {
                        val newStack = it.screenStack.toMutableList().apply { removeLast() }
                        it.copy(screenStack = newStack)
                    }
                }
            }
            is AdminHomeEvent.LocationPermissionUpdated -> {
                _state.update { it.copy(hasLocationPermission = event.isGranted) }
            }
            is AdminHomeEvent.CheckLocationPermission -> {
                val isGranted = ContextCompat.checkSelfPermission(
                    event.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                _state.update { it.copy(hasLocationPermission = isGranted) }
            }
        }
    }
}