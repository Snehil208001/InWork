package com.example.inwork.mainui.addemployeescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEmployeeState(
    val employeeId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val designation: String = "",
    val address: String = "",
    val aadharNumber: String = "",
    val mobile: String = "",
    val email: String = "",
    val office: String = "",
    val workStartTime: String = "",
    val workEndTime: String = "",
    val workEffectiveDate: String = "",
    val workEndDate: String = "",
    val isError: Boolean = false,
    val showDobPicker: Boolean = false,
    val showStartTimePicker: Boolean = false,
    val showEndTimePicker: Boolean = false,
    val showEffectiveDatePicker: Boolean = false,
    val showEndDatePicker: Boolean = false,
    val officeNames: List<String> = listOf("Meta", "TCS", "WIPRO", "HCL", "TCL", "OTHERS")
)

sealed class AddEmployeeEvent {
    data class EmployeeIdChanged(val value: String) : AddEmployeeEvent()
    data class FirstNameChanged(val value: String) : AddEmployeeEvent()
    data class LastNameChanged(val value: String) : AddEmployeeEvent()
    data class DobChanged(val value: String) : AddEmployeeEvent()
    data class DesignationChanged(val value: String) : AddEmployeeEvent()
    data class AddressChanged(val value: String) : AddEmployeeEvent()
    data class AadharNumberChanged(val value: String) : AddEmployeeEvent()
    data class MobileChanged(val value: String) : AddEmployeeEvent()
    data class EmailChanged(val value: String) : AddEmployeeEvent()
    data class OfficeChanged(val value: String) : AddEmployeeEvent()
    data class WorkStartTimeChanged(val value: String) : AddEmployeeEvent()
    data class WorkEndTimeChanged(val value: String) : AddEmployeeEvent()
    data class WorkEffectiveDateChanged(val value: String) : AddEmployeeEvent()
    data class WorkEndDateChanged(val value: String) : AddEmployeeEvent()
    object AddEmployeeClicked : AddEmployeeEvent()
    object UploadAllClicked : AddEmployeeEvent()
    object ShowDobPicker : AddEmployeeEvent()
    object DismissDobPicker : AddEmployeeEvent()
    object ShowStartTimePicker : AddEmployeeEvent()
    object DismissStartTimePicker : AddEmployeeEvent()
    object ShowEndTimePicker : AddEmployeeEvent()
    object DismissEndTimePicker : AddEmployeeEvent()
    object ShowEffectiveDatePicker : AddEmployeeEvent()
    object DismissEffectiveDatePicker : AddEmployeeEvent()
    object ShowEndDatePicker : AddEmployeeEvent()
    object DismissEndDatePicker : AddEmployeeEvent()
}

class AddEmployeeViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddEmployeeState())
    val state = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AddEmployeeEvent) {
        when (event) {
            is AddEmployeeEvent.EmployeeIdChanged -> _state.update { it.copy(employeeId = event.value) }
            is AddEmployeeEvent.FirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is AddEmployeeEvent.LastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is AddEmployeeEvent.DobChanged -> _state.update { it.copy(dob = event.value) }
            is AddEmployeeEvent.DesignationChanged -> _state.update { it.copy(designation = event.value) }
            is AddEmployeeEvent.AddressChanged -> _state.update { it.copy(address = event.value) }
            is AddEmployeeEvent.AadharNumberChanged -> _state.update { it.copy(aadharNumber = event.value) }
            is AddEmployeeEvent.MobileChanged -> _state.update { it.copy(mobile = event.value) }
            is AddEmployeeEvent.EmailChanged -> _state.update { it.copy(email = event.value) }
            is AddEmployeeEvent.OfficeChanged -> _state.update { it.copy(office = event.value) }
            is AddEmployeeEvent.WorkStartTimeChanged -> _state.update { it.copy(workStartTime = event.value) }
            is AddEmployeeEvent.WorkEndTimeChanged -> _state.update { it.copy(workEndTime = event.value) }
            is AddEmployeeEvent.WorkEffectiveDateChanged -> _state.update { it.copy(workEffectiveDate = event.value) }
            is AddEmployeeEvent.WorkEndDateChanged -> _state.update { it.copy(workEndDate = event.value) }
            AddEmployeeEvent.AddEmployeeClicked -> {
                _state.update { it.copy(isError = true) }
                val currentState = _state.value
                if (currentState.employeeId.isNotBlank() && currentState.firstName.isNotBlank()) {
                    // TODO: Add employee logic (e.g., API call)
                    println("Adding employee: ${currentState.firstName}")

                    viewModelScope.launch {
                        _navigationEvent.emit(Unit)
                    }
                }
            }
            AddEmployeeEvent.UploadAllClicked -> { /* TODO */ }
            AddEmployeeEvent.ShowDobPicker -> _state.update { it.copy(showDobPicker = true) }
            AddEmployeeEvent.DismissDobPicker -> _state.update { it.copy(showDobPicker = false) }
            AddEmployeeEvent.ShowStartTimePicker -> _state.update { it.copy(showStartTimePicker = true) }
            AddEmployeeEvent.DismissStartTimePicker -> _state.update { it.copy(showStartTimePicker = false) }
            AddEmployeeEvent.ShowEndTimePicker -> _state.update { it.copy(showEndTimePicker = true) }
            AddEmployeeEvent.DismissEndTimePicker -> _state.update { it.copy(showEndTimePicker = false) }
            AddEmployeeEvent.ShowEffectiveDatePicker -> _state.update { it.copy(showEffectiveDatePicker = true) }
            AddEmployeeEvent.DismissEffectiveDatePicker -> _state.update { it.copy(showEffectiveDatePicker = false) }
            AddEmployeeEvent.ShowEndDatePicker -> _state.update { it.copy(showEndDatePicker = true) }
            AddEmployeeEvent.DismissEndDatePicker -> _state.update { it.copy(showEndDatePicker = false) }
        }
    }
}