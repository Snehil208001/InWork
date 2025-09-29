package com.example.inwork.mainui.notificationscreen.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Represents the state of the Notification screen
data class NotificationState(
    val hasNotificationPermission: Boolean = false
)

// Represents events that can be triggered from the UI
sealed class NotificationEvent {
    data class OnPermissionResult(val isGranted: Boolean) : NotificationEvent()
    data class CheckInitialPermission(val context: Context) : NotificationEvent()
}

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.OnPermissionResult -> {
                _state.update { it.copy(hasNotificationPermission = event.isGranted) }
            }
            is NotificationEvent.CheckInitialPermission -> {
                checkPermission(event.context)
            }
        }
    }

    private fun checkPermission(context: Context) {
        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permissions are implicitly granted on older versions
        }
        _state.update { it.copy(hasNotificationPermission = isGranted) }
    }
}