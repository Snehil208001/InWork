package com.example.inwork.mainui.authenticationscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.mainui.authenticationscreen.ui.LoginRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Represents the state of the Login screen
data class AuthState(
    val selectedRole: LoginRole = LoginRole.ADMIN,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isForgotPasswordDialogVisible: Boolean = false
)

// Represents the events/actions a user can perform on the screen
sealed class AuthEvent {
    data class RoleSelected(val role: LoginRole) : AuthEvent()
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    object TogglePasswordVisibility : AuthEvent()
    object LoginClicked : AuthEvent()
    object ForgotPasswordClicked : AuthEvent()
    object DismissForgotPasswordDialog : AuthEvent()
    data class RecoverPassword(val email: String) : AuthEvent()
    object RegisterNowClicked : AuthEvent()
}

// Represents one-time navigation events
sealed class AuthNavigationEvent {
    object NavigateToSignup : AuthNavigationEvent()
    object NavigateToOnboarding : AuthNavigationEvent()
}

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> _state.update { it.copy(email = event.email) }
            is AuthEvent.PasswordChanged -> _state.update { it.copy(password = event.password) }
            is AuthEvent.RoleSelected -> _state.update { it.copy(selectedRole = event.role) }
            AuthEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            AuthEvent.ForgotPasswordClicked -> _state.update { it.copy(isForgotPasswordDialogVisible = true) }
            AuthEvent.DismissForgotPasswordDialog -> _state.update { it.copy(isForgotPasswordDialogVisible = false) }
            is AuthEvent.RecoverPassword -> {
                // TODO: Add your password recovery logic here (e.g., call your backend API)
                println("Password recovery requested for: ${event.email}") // Placeholder action
                _state.update { it.copy(isForgotPasswordDialogVisible = false) }
            }
            AuthEvent.LoginClicked -> {
                // TODO: Add actual login validation logic here
                viewModelScope.launch {
                    _navigationEvent.emit(AuthNavigationEvent.NavigateToOnboarding)
                }
            }
            AuthEvent.RegisterNowClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(AuthNavigationEvent.NavigateToSignup)
                }
            }
        }
    }
}