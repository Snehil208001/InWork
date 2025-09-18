package com.example.inwork.mainui.admineventscreen.ui

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
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
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventViewModel
import com.example.inwork.mainui.admineventscreen.viewmodel.AddEventEvent
import com.example.inwork.mainui.eventscreen.ui.CalendarView // Correct import
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEventViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNavigateBack()
        }
    }

    if (state.showEventDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEventEvent.DismissEventDatePicker) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onEvent(AddEventEvent.EventDateChanged(millis.toFormattedDate()))
                    }
                    viewModel.onEvent(AddEventEvent.DismissEventDatePicker)
                }) { Text("OK") }
            },
            dismissButton = {
                Button(onClick = { viewModel.onEvent(AddEventEvent.DismissEventDatePicker) }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (state.showStartTimePicker) {
        ShowTimePicker(context) { time ->
            viewModel.onEvent(AddEventEvent.EventStartTimeChanged(time))
            viewModel.onEvent(AddEventEvent.DismissStartTimePicker)
        }
    }

    if (state.showEndTimePicker) {
        ShowTimePicker(context) { time ->
            viewModel.onEvent(AddEventEvent.EventEndTimeChanged(time))
            viewModel.onEvent(AddEventEvent.DismissEndTimePicker)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { newDate -> selectedDate = newDate }
        )
        Spacer(modifier = Modifier.height(24.dp))
        FormInputField(
            value = state.eventName,
            onValueChange = { viewModel.onEvent(AddEventEvent.EventNameChanged(it)) },
            label = "Event Name",
            isError = state.isError && state.eventName.isBlank()
        )
        FormInputField(
            value = state.eventDescription,
            onValueChange = { viewModel.onEvent(AddEventEvent.EventDescriptionChanged(it)) },
            label = "Event Description",
            isError = state.isError && state.eventDescription.isBlank()
        )
        IconInputField(
            value = state.eventDate,
            onValueChange = {},
            label = "Date (YYYY-MM-DD)",
            icon = Icons.Default.CalendarToday,
            isError = state.isError && state.eventDate.isBlank(),
            onIconClick = { viewModel.onEvent(AddEventEvent.ShowEventDatePicker) }
        )
        IconInputField(
            value = state.eventStartTime,
            onValueChange = {},
            label = "Start Time (24h)",
            icon = Icons.Default.AccessTime,
            isError = state.isError && state.eventStartTime.isBlank(),
            onIconClick = { viewModel.onEvent(AddEventEvent.ShowStartTimePicker) }
        )
        IconInputField(
            value = state.eventEndTime,
            onValueChange = {},
            label = "End Time (24h)",
            icon = Icons.Default.AccessTime,
            isError = state.isError && state.eventEndTime.isBlank(),
            onIconClick = { viewModel.onEvent(AddEventEvent.ShowEndTimePicker) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.onEvent(AddEventEvent.AddEventClicked) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
        ) {
            Text("ADD EVENT", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
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
        true
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen(onNavigateBack = {})
}