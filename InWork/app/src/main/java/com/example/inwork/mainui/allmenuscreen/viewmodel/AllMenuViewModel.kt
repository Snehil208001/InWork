package com.example.inwork.mainui.allmenuscreen.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.inwork.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// MenuItem now includes a route for navigation
data class MenuItem(val title: String, val icon: ImageVector, val route: String)

// Represents the state for the AllMenuScreen
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
        _state.value = AllMenuState(
            generalItems = listOf(
                MenuItem("Home", Icons.Default.Home, Screen.adminHome.route),
                MenuItem("Profile", Icons.Default.Person, Screen.Profile.route),
                MenuItem("News", Icons.Default.Newspaper, Screen.News.route),
                MenuItem("Check Weather", Icons.Default.WbSunny, Screen.Weather.route)
            ),
            managementItems = listOf(
                MenuItem("All Employees", Icons.Default.Groups, Screen.AllEmployees.route),
                MenuItem("Add Employee", Icons.Default.PersonAdd, Screen.AddEmployee.route),
                MenuItem("Employee Status", Icons.Default.Work, Screen.EmployeeStatus.route),
                MenuItem("Leave Request", Icons.Default.CalendarToday, Screen.LeaveRequest.route),
                MenuItem("Monthly Reports", Icons.Default.Assessment, Screen.MonthlyReports.route),
                MenuItem("All Offices", Icons.Default.LocationCity, Screen.AllOffices.route),
                MenuItem("Add Offices", Icons.Default.Business, Screen.AddOffice.route),
                MenuItem("Screen Time Details", Icons.Default.Smartphone, Screen.ScreenTime.route)
            ),
            communicationItems = listOf(
                MenuItem("Sent Notices", Icons.Default.Send, Screen.SentNotices.route),
                MenuItem("Add Event", Icons.Default.Event, Screen.AddEvent.route)
            ),
            supportItems = listOf(
                MenuItem("Contact Us", Icons.Default.Call, Screen.ContactUs.route),
                MenuItem("Settings", Icons.Default.Settings, Screen.AdminSettingsScreen.route)
            )
        )
    }
}