package com.example.inwork.mainui.dashboardscreen.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data class to hold the state for the entire dashboard
data class DashboardState(
    val checkInData: List<Float> = listOf(10f, 20f, 30f),
    val checkOutData: List<Float> = listOf(5f, 10f, 20f),
    val checkInOutMonths: List<String> = listOf("Jan", "Feb", "Mar"),
    val activityChartData: Map<String, Float> = mapOf(
        "Instagram" to 0.4f,
        "Facebook" to 0.3f,
        "WhatsApp" to 0.3f
    ),
    val performanceData: List<Pair<Float, Float>> = listOf(
        1.2f to 100f, 1.8f to 110f, 2.1f to 108f, 2.7f to 105f
    )
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()
}