package com.example.inwork.mainui.leavescreen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inwork.mainui.addemployeescreen.ui.IconInputField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostLeaveScreen(
    onPostLeave: (designation: String, fromDate: String, toDate: String, reason: String) -> Unit
) {
    var designation by remember { mutableStateOf("") }
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var leaveReason by remember { mutableStateOf("") }

    var isDesignationError by remember { mutableStateOf(false) }
    var isFromDateError by remember { mutableStateOf(false) }
    var isToDateError by remember { mutableStateOf(false) }
    var isReasonError by remember { mutableStateOf(false) }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    // Date picker dialogs
    if (showFromDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showFromDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fromDate = millis.toFormattedDate()
                    }
                    showFromDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showToDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showToDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        toDate = millis.toFormattedDate()
                    }
                    showToDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        FormInputField(
            value = designation,
            onValueChange = { designation = it; isDesignationError = it.isBlank() },
            label = "Designation",
            isError = isDesignationError
        )
        IconInputField(
            value = fromDate,
            onValueChange = {},
            label = "From (YYYY-MM-DD)",
            icon = Icons.Default.CalendarToday,
            isError = isFromDateError,
            onIconClick = { showFromDatePicker = true }
        )
        IconInputField(
            value = toDate,
            onValueChange = {},
            label = "To (YYYY-MM-DD)",
            icon = Icons.Default.CalendarToday,
            isError = isToDateError,
            onIconClick = { showToDatePicker = true }
        )
        FormInputField(
            value = leaveReason,
            onValueChange = { leaveReason = it; isReasonError = it.isBlank() },
            label = "Leave Reason",
            isError = isReasonError
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                isDesignationError = designation.isBlank()
                isFromDateError = fromDate.isBlank()
                isToDateError = toDate.isBlank()
                isReasonError = leaveReason.isBlank()

                if (!isDesignationError && !isFromDateError && !isToDateError && !isReasonError) {
                    onPostLeave(designation, fromDate, toDate, leaveReason)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
        ) {
            Text("POST LEAVE", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Reusing FormInputField for consistency
@Composable
fun FormInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            shape = RoundedCornerShape(12.dp)
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


private fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostLeaveScreenPreview() {
    PostLeaveScreen(onPostLeave = { _, _, _, _ -> })
}