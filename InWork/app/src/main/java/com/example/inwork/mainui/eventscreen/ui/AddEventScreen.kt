package com.example.inwork.mainui.eventscreen.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(onSaveEvent: (date: LocalDate, event: String) -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalendarView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Add Event") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (eventName.isNotBlank()) {
                    onSaveEvent(selectedDate, eventName)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009B4D))
        ) {
            Text("SAVE EVENT", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarView(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
    val days = (1..daysInMonth).toList()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                Text(it, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(7), userScrollEnabled = false) {
            items(firstDayOfMonth) { Spacer(modifier = Modifier.size(40.dp)) }
            items(days) { day ->
                val date = currentMonth.atDay(day)
                val isSelected = date == selectedDate
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF4CAF50) else Color.Transparent)
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = day.toString(), color = if (isSelected) Color.White else Color.Black)
                }
            }
        }
    }
}