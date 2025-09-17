package com.example.inwork.mainui.addofficescreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddOfficeState(
    val latitude: String = "",
    val longitude: String = "",
    val officeName: String = "",
    val radius: String = "",
    val isOfficeNameError: Boolean = false,
    val isRadiusError: Boolean = false,
    val isLocationError: Boolean = false,
    val formSubmitted: Boolean = false
)

sealed class AddOfficeEvent {
    data class OnOfficeNameChange(val name: String) : AddOfficeEvent()
    data class OnRadiusChange(val radius: String) : AddOfficeEvent()
    data class OnLocationSet(val lat: Double, val long: Double) : AddOfficeEvent()
    object OnAddOfficeClick : AddOfficeEvent()
}

class AddOfficeViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddOfficeState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddOfficeEvent) {
        when (event) {
            is AddOfficeEvent.OnOfficeNameChange -> {
                _state.update { it.copy(officeName = event.name, isOfficeNameError = false) }
            }
            is AddOfficeEvent.OnRadiusChange -> {
                if (event.radius.all { it.isDigit() }) {
                    _state.update { it.copy(radius = event.radius, isRadiusError = false) }
                }
            }
            is AddOfficeEvent.OnLocationSet -> {
                _state.update {
                    it.copy(
                        latitude = String.format("%.6f", event.lat),
                        longitude = String.format("%.6f", event.long),
                        isLocationError = false
                    )
                }
            }
            AddOfficeEvent.OnAddOfficeClick -> {
                validateAndSubmit()
            }
        }
    }

    private fun validateAndSubmit() {
        val currentState = _state.value
        val officeNameBlank = currentState.officeName.isBlank()
        val radiusBlank = currentState.radius.isBlank()
        val locationBlank = currentState.latitude.isBlank()

        if (officeNameBlank || radiusBlank || locationBlank) {
            _state.update {
                it.copy(
                    isOfficeNameError = officeNameBlank,
                    isRadiusError = radiusBlank,
                    isLocationError = locationBlank
                )
            }
        } else {
            // TODO: Implement API call to save the office data
            println("Adding Office: Name='${currentState.officeName}', Lat='${currentState.latitude}', Long='${currentState.longitude}', Radius='${currentState.radius}'")
            _state.update { it.copy(formSubmitted = true) }
        }
    }
}