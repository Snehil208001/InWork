package com.example.inwork.mainui.allemployeesscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inwork.mainui.allemployeesscreen.ui.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the state of the AllEmployeesScreen.
 * @property employees The list of employees to display.
 * @property searchQuery The current text in the search input field.
 */
data class AllEmployeesState(
    val employees: List<Employee> = emptyList(),
    val searchQuery: String = ""
)

/**
 * ViewModel for the AllEmployeesScreen.
 * Handles the business logic and state management for the screen.
 */
class AllEmployeesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AllEmployeesState())
    val uiState: StateFlow<AllEmployeesState> = _uiState.asStateFlow()

    init {
        // In a real application, you would fetch this data from a repository or database.
        loadEmployees()
    }

    /**
     * Updates the search query in the state.
     * @param query The new search text.
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            // This is sample data. In a real app, this would be an API call or database query.
            val employeeList = listOf(
                Employee("Moti123", "Motilal", "Das", "123", "nbs", "Active"),
                Employee("Raju123", "Raju", "Rathor", "123", "nbs", "Active"),
                Employee("s123", "sambhu", "nath", "dbnd", "hdjdjd", "Inactive"),
                Employee("job123", "ratan", "t", "2025-01-03", "gxgc", "Active"),
                Employee("ratan123", "Ratan", "thakur", "2025-01-05", "hj", "Active"),
                Employee("sabi123", "Sabi", "mahato", "2025-03-08", "dhjdjd", "Inactive")
            )
            _uiState.update { it.copy(employees = employeeList) }
        }
    }
}