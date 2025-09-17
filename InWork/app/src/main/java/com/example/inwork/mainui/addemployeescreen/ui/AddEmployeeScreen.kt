package com.example.inwork.mainui.addemployeescreen.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.addemployeescreen.viewmodel.AddEmployeeEvent
import com.example.inwork.mainui.addemployeescreen.viewmodel.AddEmployeeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEmployeeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNavigateBack()
        }
    }

    // --- Date and Time Picker Dialogs ---

    if (state.showDobPicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEmployeeEvent.DismissDobPicker) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onEvent(AddEmployeeEvent.DobChanged(millis.toFormattedDate()))
                    }
                    viewModel.onEvent(AddEmployeeEvent.DismissDobPicker)
                }) { Text("OK") }
            },
            dismissButton = {
                Button(onClick = { viewModel.onEvent(AddEmployeeEvent.DismissDobPicker) }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showEffectiveDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEmployeeEvent.DismissEffectiveDatePicker) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onEvent(AddEmployeeEvent.WorkEffectiveDateChanged(millis.toFormattedDate()))
                    }
                    viewModel.onEvent(AddEmployeeEvent.DismissEffectiveDatePicker)
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEmployeeEvent.DismissEndDatePicker) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onEvent(AddEmployeeEvent.WorkEndDateChanged(millis.toFormattedDate()))
                    }
                    viewModel.onEvent(AddEmployeeEvent.DismissEndDatePicker)
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showStartTimePicker) {
        ShowTimePicker(context) { time ->
            viewModel.onEvent(AddEmployeeEvent.WorkStartTimeChanged(time))
            viewModel.onEvent(AddEmployeeEvent.DismissStartTimePicker)
        }
    }

    if (state.showEndTimePicker) {
        ShowTimePicker(context) { time ->
            viewModel.onEvent(AddEmployeeEvent.WorkEndTimeChanged(time))
            viewModel.onEvent(AddEmployeeEvent.DismissEndTimePicker)
        }
    }

    if (state.showOfficeDialog) {
        OfficeSelectionDialog(
            onDismiss = { viewModel.onEvent(AddEmployeeEvent.DismissOfficeDialog) },
            onOfficeSelected = { selectedOffice ->
                viewModel.onEvent(AddEmployeeEvent.OfficeChanged(selectedOffice))
                viewModel.onEvent(AddEmployeeEvent.DismissOfficeDialog)
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Form fields
        FormInputField(value = state.employeeId, onValueChange = { viewModel.onEvent(AddEmployeeEvent.EmployeeIdChanged(it)) }, label = "Employee ID", isError = state.isError && state.employeeId.isBlank())
        FormInputField(value = state.firstName, onValueChange = { viewModel.onEvent(AddEmployeeEvent.FirstNameChanged(it)) }, label = "First Name", isError = state.isError && state.firstName.isBlank())
        FormInputField(value = state.lastName, onValueChange = { viewModel.onEvent(AddEmployeeEvent.LastNameChanged(it)) }, label = "Last Name", isError = state.isError && state.lastName.isBlank())
        IconInputField(value = state.dob, onValueChange = {}, label = "D.O.B (YYYY-MM-DD)", icon = Icons.Default.CalendarToday, isError = state.isError && state.dob.isBlank(), onIconClick = { viewModel.onEvent(AddEmployeeEvent.ShowDobPicker) })
        FormInputField(value = state.designation, onValueChange = { viewModel.onEvent(AddEmployeeEvent.DesignationChanged(it)) }, label = "Designation", isError = state.isError && state.designation.isBlank())
        FormInputField(value = state.address, onValueChange = { viewModel.onEvent(AddEmployeeEvent.AddressChanged(it)) }, label = "Address", isError = state.isError && state.address.isBlank())
        FormInputField(value = state.aadharNumber, onValueChange = { viewModel.onEvent(AddEmployeeEvent.AadharNumberChanged(it)) }, label = "Aadhaar Number", isError = state.isError && state.aadharNumber.isBlank())
        FormInputField(value = state.mobile, onValueChange = { viewModel.onEvent(AddEmployeeEvent.MobileChanged(it)) }, label = "Mobile", isError = state.isError && state.mobile.isBlank())
        FormInputField(value = state.email, onValueChange = { viewModel.onEvent(AddEmployeeEvent.EmailChanged(it)) }, label = "Email", isError = state.isError && state.email.isBlank())

        Box {
            IconInputField(
                value = state.office,
                onValueChange = {},
                label = "Click On it to Add Office",
                icon = null,
                isError = state.isError && state.office.isBlank(),
                readOnly = true,
                onIconClick = {},
                modifier = Modifier.clickable { viewModel.onEvent(AddEmployeeEvent.ShowOfficeDialog) }
            )
            IconButton(
                onClick = { /* TODO: Handle Add More Office click */ },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "Add More Office")
                    Text("AddMoreOffice", fontSize = 8.sp)
                }
            }
        }

        IconInputField(value = state.workStartTime, onValueChange = {}, label = "Work Start Time", icon = Icons.Default.AccessTime, isError = state.isError && state.workStartTime.isBlank(), onIconClick = { viewModel.onEvent(AddEmployeeEvent.ShowStartTimePicker) })
        IconInputField(value = state.workEndTime, onValueChange = {}, label = "Work End Time", icon = Icons.Default.AccessTime, isError = state.isError && state.workEndTime.isBlank(), onIconClick = { viewModel.onEvent(AddEmployeeEvent.ShowEndTimePicker) })
        IconInputField(value = state.workEffectiveDate, onValueChange = {}, label = "Work Effective From Date (YYYY-MM-DD)", icon = Icons.Default.CalendarToday, isError = state.isError && state.workEffectiveDate.isBlank(), onIconClick = { viewModel.onEvent(AddEmployeeEvent.ShowEffectiveDatePicker) })
        IconInputField(value = state.workEndDate, onValueChange = {}, label = "Work End From Date (YYYY-MM-DD)", icon = Icons.Default.CalendarToday, isError = state.isError && state.workEndDate.isBlank(), onIconClick = { viewModel.onEvent(AddEmployeeEvent.ShowEndDatePicker) })

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.onEvent(AddEmployeeEvent.AddEmployeeClicked) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
            ) {
                Text("ADD EMPLOYEE", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.onEvent(AddEmployeeEvent.UploadAllClicked) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
            ) {
                Text("UPLOAD ALL", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

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

@Composable
fun IconInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector?,
    isError: Boolean,
    readOnly: Boolean = true,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            readOnly = readOnly,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = icon?.let {
                {
                    IconButton(onClick = onIconClick) {
                        Icon(
                            imageVector = it,
                            contentDescription = label,
                            tint = Color.White,
                            modifier = Modifier
                                .background(Color(0xFF009B4D), CircleShape)
                                .padding(6.dp)
                        )
                    }
                }
            }
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

@Composable
private fun ShowTimePicker(
    context: android.content.Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected("$selectedHour:$selectedMinute")
        },
        hour,
        minute,
        true // 24-hour view
    )

    LaunchedEffect(Unit) {
        timePickerDialog.show()
    }
}

private fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}

@Composable
fun OfficeSelectionDialog(
    onDismiss: () -> Unit,
    onOfficeSelected: (String) -> Unit
) {
    var officeName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Office") },
        text = {
            OutlinedTextField(
                value = officeName,
                onValueChange = { officeName = it },
                label = { Text("Office Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onOfficeSelected(officeName) }) {
                Text("Select")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEmployeeScreenPreview() {
    AddEmployeeScreen(onNavigateBack = {})
}