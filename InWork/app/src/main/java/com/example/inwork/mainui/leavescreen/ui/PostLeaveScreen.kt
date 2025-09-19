package com.example.inwork.mainui.leavescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.leavescreen.viewmodel.PostLeaveEvent
import com.example.inwork.mainui.leavescreen.viewmodel.PostLeaveViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostLeaveScreen(
    viewModel: PostLeaveViewModel = viewModel(),
    onPostLeave: (designation: String, fromDate: String, toDate: String, reason: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showFromDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(PostLeaveEvent.DismissFromDatePicker) },
            confirmButton = {
                Button(onClick = {
                    // UPDATED: Send the raw millis to the ViewModel
                    viewModel.onEvent(PostLeaveEvent.FromDateSelected(datePickerState.selectedDateMillis))
                    viewModel.onEvent(PostLeaveEvent.DismissFromDatePicker)
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (uiState.showToDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(PostLeaveEvent.DismissToDatePicker) },
            confirmButton = {
                Button(onClick = {
                    // UPDATED: Send the raw millis to the ViewModel
                    viewModel.onEvent(PostLeaveEvent.ToDateSelected(datePickerState.selectedDateMillis))
                    viewModel.onEvent(PostLeaveEvent.DismissToDatePicker)
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FormInputField(
            value = uiState.designation,
            onValueChange = { viewModel.onEvent(PostLeaveEvent.DesignationChanged(it)) },
            label = "Designation",
            isError = uiState.isDesignationError
        )

        DateFieldWithIcon(
            value = uiState.fromDate,
            label = "From(YYYY-MM-DD)",
            isError = uiState.isFromDateError,
            onIconClick = { viewModel.onEvent(PostLeaveEvent.ShowFromDatePicker) }
        )

        DateFieldWithIcon(
            value = uiState.toDate,
            label = "To(YYYY-MM-DD)",
            isError = uiState.isToDateError,
            onIconClick = { viewModel.onEvent(PostLeaveEvent.ShowToDatePicker) }
        )

        FormInputField(
            value = uiState.leaveReason,
            onValueChange = { viewModel.onEvent(PostLeaveEvent.LeaveReasonChanged(it)) },
            label = "Leave Reason",
            isError = uiState.isReasonError
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.onEvent(PostLeaveEvent.PostLeaveClicked)
                val currentState = viewModel.uiState.value // Re-check the latest state
                if (!currentState.isDesignationError && !currentState.isFromDateError && !currentState.isToDateError && !currentState.isReasonError && currentState.designation.isNotBlank()) {
                    onPostLeave(currentState.designation, currentState.fromDate, currentState.toDate, currentState.leaveReason)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("POST LEAVE", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DateFieldWithIcon(
    value: String,
    label: String,
    isError: Boolean,
    onIconClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                label = { Text(label) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { onIconClick() },
                shape = RoundedCornerShape(8.dp),
                readOnly = true,
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            IconButton(
                onClick = onIconClick,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF00ACC1), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Select Date",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        if (isError) {
            Text(
                text = "Required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun FormInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean
) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            )
        )
        if (isError) {
            Text(
                text = "Required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// REMOVED the toFormattedDate() function from this file.

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostLeaveScreenPreview() {
    PostLeaveScreen(onPostLeave = { _, _, _, _ -> })
}