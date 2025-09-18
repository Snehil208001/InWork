package com.example.inwork.mainui.admineventscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEventState(
    val eventName: String = "",
    val eventDescription: String = "",
    val eventDate: String = "",
    val eventStartTime: String = "",
    val eventEndTime: String = "",
    val isError: Boolean = false,
    val showEventDatePicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false
)

sealed class AddEventEvent {
    data class EventNameChanged(val value: String) : AddEventEvent()
    data class EventDescriptionChanged(val value: String) : AddEventEvent()
    data class EventDateChanged(val value: String) : AddEventEvent()
    data class EventStartTimeChanged(val value: String) : AddEventEvent()
    data class EventEndTimeChanged(val value: String) : AddEventEvent()
    object AddEventClicked : AddEventEvent()
    object ShowEventDatePicker : AddEventEvent()
    object DismissEventDatePicker : AddEventEvent()
    object ShowStartTimePicker : AddEventEvent()
    object DismissStartTimePicker : AddEventEvent()
    object ShowEndTimePicker : AddEventEvent()
    object DismissEndTimePicker : AddEventEvent()
}

class AddEventViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddEventState())
    val state = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AddEventEvent) {
        when (event) {
            is AddEventEvent.EventNameChanged -> _state.update { it.copy(eventName = event.value) }
            is AddEventEvent.EventDescriptionChanged -> _state.update { it.copy(eventDescription = event.value) }
            is AddEventEvent.EventDateChanged -> _state.update { it.copy(eventDate = event.value) }
            is AddEventEvent.EventStartTimeChanged -> _state.update { it.copy(eventStartTime = event.value) }
            is AddEventEvent.EventEndTimeChanged -> _state.update { it.copy(eventEndTime = event.value) }
            AddEventEvent.AddEventClicked -> {
                _state.update { it.copy(isError = true) }
                val currentState = _state.value
                if (currentState.eventName.isNotBlank() && currentState.eventDescription.isNotBlank() && currentState.eventDate.isNotBlank() && currentState.eventStartTime.isNotBlank() && currentState.eventEndTime.isNotBlank()) {
                    // TODO: Add event logic
                    println("Adding event: ${currentState.eventName}")

                    viewModelScope.launch {
                        _navigationEvent.emit(Unit)
                    }
                }
            }
            AddEventEvent.ShowEventDatePicker -> _state.update { it.copy(showEventDatePicker = true) }
            AddEventEvent.DismissEventDatePicker -> _state.update { it.copy(showEventDatePicker = false) }
            AddEventEvent.ShowStartTimePicker -> _state.update { it.copy(showStartTimePicker = true) }
            AddEventEvent.DismissStartTimePicker -> _state.update { it.copy(showStartTimePicker = false) }
            AddEventEvent.ShowEndTimePicker -> _state.update { it.copy(showEndTimePicker = true) }
            AddEventEvent.DismissEndTimePicker -> _state.update { it.copy(showEndTimePicker = false) }
        }
    }
}