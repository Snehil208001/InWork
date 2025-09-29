package com.example.inwork.mainui.allemployeesscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.allemployeesscreen.viewmodel.AllEmployeesState
import com.example.inwork.mainui.allemployeesscreen.viewmodel.AllEmployeesViewModel

// Data class remains the same
data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val dob: String,
    val designation: String,
    val status: String
)

@Composable
fun AllEmployeesScreen(
    navController: NavController,
    viewModel: AllEmployeesViewModel = hiltViewModel() // Use Hilt to get the ViewModel instance
) {
    // Observe the state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Pass the state and event handlers to the stateless content composable
    AllEmployeesContent(
        state = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange
    )
}

@Composable
fun AllEmployeesContent(
    modifier: Modifier = Modifier,
    state: AllEmployeesState,
    onSearchQueryChange: (String) -> Unit
) {
    // Filter the employees based on the search query from the state
    val filteredEmployees = remember(state.searchQuery, state.employees) {
        if (state.searchQuery.isBlank()) {
            state.employees
        } else {
            state.employees.filter {
                it.firstName.contains(state.searchQuery, ignoreCase = true)
                        || it.id.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F0))
            .statusBarsPadding() // ✅ CORRECTED: Added padding for the status bar/cutout
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange, // Call the event handler on text change
            label = { Text("Search Employee...") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredEmployees) { employee ->
                EmployeeCard(employee = employee)
            }
        }
    }
}

@Composable
fun EmployeeCard(employee: Employee) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Employee ID: ${employee.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Button(
                    onClick = { /* TODO: Handle photo upload */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC8E6C9)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("UPLOAD PHOTO", color = Color.Black, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            EmployeeDetailRow("First Name:", employee.firstName)
            EmployeeDetailRow("Last Name:", employee.lastName)
            EmployeeDetailRow("Date of Birth:", employee.dob)
            EmployeeDetailRow("Designation:", employee.designation)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ${employee.status}",
                    color = if (employee.status == "Active") Color(0xFF4CAF50) else Color.Red
                )
                TextButton(onClick = { /* TODO: Handle view more */ }) {
                    Text("View More>>>>>>", color = Color.Blue)
                }
            }
        }
    }
}

@Composable
fun EmployeeDetailRow(label: String, value: String) {
    Row {
        Text(text = label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
        Text(text = value)
    }
}

@Preview(showBackground = true)
@Composable
fun AllEmployeesScreenPreview() {
    // The preview now shows the stateless content composable with sample data
    val previewState = AllEmployeesState(
        employees = listOf(
            Employee("Moti123", "Motilal", "Das", "123", "nbs", "Active"),
            Employee("Raju123", "Raju", "Rathor", "123", "nbs", "Active")
        ),
        searchQuery = ""
    )
    AllEmployeesContent(state = previewState, onSearchQueryChange = {})
}