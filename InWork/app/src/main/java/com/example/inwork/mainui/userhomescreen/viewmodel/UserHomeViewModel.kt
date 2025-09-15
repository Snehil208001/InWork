package com.example.inwork.mainui.userhomescreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.inwork.mainui.userhomescreen.ui.UserScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the state of the UserHomeScreen.
 * @property currentScreen The screen currently being displayed.
 */
data class UserHomeState(
    val currentScreen: UserScreen = UserScreen.Home
)

/**
 * Defines the events that can be sent from the UI to the ViewModel.
 */
sealed class UserHomeEvent {
    /**
     * Event triggered when a new screen is selected from the bottom navigation bar.
     * @param screen The new screen to display.
     */
    data class ScreenSelected(val screen: UserScreen) : UserHomeEvent()
}

/**
 * ViewModel for the UserHomeScreen.
 */
class UserHomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(UserHomeState())
    val state: StateFlow<UserHomeState> = _state.asStateFlow()

    /**
     * Handles events sent from the UI.
     * @param event The event to handle.
     */
    fun onEvent(event: UserHomeEvent) {
        when (event) {
            is UserHomeEvent.ScreenSelected -> {
                _state.update { it.copy(currentScreen = event.screen) }
            }
        }
    }
}