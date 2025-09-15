package com.example.inwork.mainui.allmenuscreen.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Moved the MenuItem data class here to be with its related logic
data class MenuItem(val title: String, val icon: ImageVector)

// Represents the state for the AllMenuScreen, holding all the item lists
data class AllMenuState(
    val generalItems: List<MenuItem> = emptyList(),
    val managementItems: List<MenuItem> = emptyList(),
    val communicationItems: List<MenuItem> = emptyList(),
    val supportItems: List<MenuItem> = emptyList()
)

class AllMenuViewModel : ViewModel() {

    private val _state = MutableStateFlow(AllMenuState())
    val state: StateFlow<AllMenuState> = _state.asStateFlow()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {
        // Category 1: General & Personal
        val generalItems = listOf(
            MenuItem("Home", Icons.Default.Home),
            MenuItem("Profile", Icons.Default.Person),
            MenuItem("News", Icons.Default.Newspaper),
            MenuItem("Check Weather", Icons.Default.WbSunny)
        )

        // Category 2: Employee & Office Management
        val managementItems = listOf(
            MenuItem("All Employees", Icons.Default.Groups),
            MenuItem("Add Employee", Icons.Default.PersonAdd),
            MenuItem("Employee Status", Icons.Default.Work),
            MenuItem("Leave Request", Icons.Default.CalendarToday),
            MenuItem("Monthly Reports", Icons.Default.Assessment),
            MenuItem("All Offices", Icons.Default.LocationCity),
            MenuItem("Add Offices", Icons.Default.Business),
            MenuItem("Screen Time Details", Icons.Default.Smartphone)
        )

        // Category 3: Communication & Events
        val communicationItems = listOf(
            MenuItem("Sent Notices", Icons.Default.Send),
            MenuItem("Add Event", Icons.Default.Event)
        )

        // Category 4: App Support
        val supportItems = listOf(
            MenuItem("Contact Us", Icons.Default.Call),
            MenuItem("Settings", Icons.Default.Settings)
        )

        _state.value = AllMenuState(
            generalItems = generalItems,
            managementItems = managementItems,
            communicationItems = communicationItems,
            supportItems = supportItems
        )
    }
}