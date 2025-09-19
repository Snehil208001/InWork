package com.example.inwork.mainui.leavescreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PostLeaveUiState(
    val designation: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val leaveReason: String = "",
    val isDesignationError: Boolean = false,
    val isFromDateError: Boolean = false,
    val isToDateError: Boolean = false,
    val isReasonError: Boolean = false,
    val showFromDatePicker: Boolean = false,
    val showToDatePicker: Boolean = false
)

sealed class PostLeaveEvent {
    data class DesignationChanged(val designation: String) : PostLeaveEvent()
    data class FromDateSelected(val dateMillis: Long?) : PostLeaveEvent() // CHANGED
    data class ToDateSelected(val dateMillis: Long?) : PostLeaveEvent()   // CHANGED
    data class LeaveReasonChanged(val reason: String) : PostLeaveEvent()
    object ShowFromDatePicker : PostLeaveEvent()
    object ShowToDatePicker : PostLeaveEvent()
    object DismissFromDatePicker : PostLeaveEvent()
    object DismissToDatePicker : PostLeaveEvent()
    object PostLeaveClicked : PostLeaveEvent()
}

class PostLeaveViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PostLeaveUiState())
    val uiState: StateFlow<PostLeaveUiState> = _uiState.asStateFlow()

    fun onEvent(event: PostLeaveEvent) {
        when (event) {
            is PostLeaveEvent.DesignationChanged -> {
                _uiState.update { it.copy(designation = event.designation, isDesignationError = event.designation.isBlank()) }
            }
            is PostLeaveEvent.FromDateSelected -> { // CHANGED
                val fromDateString = event.dateMillis?.toFormattedDate() ?: _uiState.value.fromDate
                _uiState.update { it.copy(fromDate = fromDateString, isFromDateError = fromDateString.isBlank()) }
            }
            is PostLeaveEvent.ToDateSelected -> { // CHANGED
                val toDateString = event.dateMillis?.toFormattedDate() ?: _uiState.value.toDate
                _uiState.update { it.copy(toDate = toDateString, isToDateError = toDateString.isBlank()) }
            }
            is PostLeaveEvent.LeaveReasonChanged -> {
                _uiState.update { it.copy(leaveReason = event.reason, isReasonError = event.reason.isBlank()) }
            }
            PostLeaveEvent.ShowFromDatePicker -> {
                _uiState.update { it.copy(showFromDatePicker = true) }
            }
            PostLeaveEvent.ShowToDatePicker -> {
                _uiState.update { it.copy(showToDatePicker = true) }
            }
            PostLeaveEvent.DismissFromDatePicker -> {
                _uiState.update { it.copy(showFromDatePicker = false) }
            }
            PostLeaveEvent.DismissToDatePicker -> {
                _uiState.update { it.copy(showToDatePicker = false) }
            }
            PostLeaveEvent.PostLeaveClicked -> {
                val state = _uiState.value
                val hasError = state.designation.isBlank() || state.fromDate.isBlank() || state.toDate.isBlank() || state.leaveReason.isBlank()
                if (!hasError) {
                    // onPostLeave(state.designation, state.fromDate, state.toDate, state.leaveReason)
                } else {
                    _uiState.update {
                        it.copy(
                            isDesignationError = state.designation.isBlank(),
                            isFromDateError = state.fromDate.isBlank(),
                            isToDateError = state.toDate.isBlank(),
                            isReasonError = state.leaveReason.isBlank()
                        )
                    }
                }
            }
        }
    }

    // MOVED the formatting function here
    private fun Long.toFormattedDate(): String {
        val date = Date(this)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}