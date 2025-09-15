package com.example.inwork.mainui.adminhomescreen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Data class to hold information for each menu item
data class MenuItem(val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllMenuScreen(navController: NavController) {
    // List of all menu items from your images
    val menuItems = listOf(
        MenuItem("Home", Icons.Default.Home),
        MenuItem("Profile", Icons.Default.Person),
        MenuItem("All Employees", Icons.Default.Groups),
        MenuItem("Sent Notices", Icons.Default.Send),
        MenuItem("Add Event", Icons.Default.Event),
        MenuItem("Employee Status", Icons.Default.Work),
        MenuItem("Leave Request", Icons.Default.CalendarToday),
        MenuItem("Monthly Reports", Icons.Default.Assessment),
        MenuItem("Add Employee", Icons.Default.PersonAdd),
        MenuItem("Add Offices", Icons.Default.Business),
        MenuItem("All Offices", Icons.Default.LocationCity),
        MenuItem("Screen Time Details", Icons.Default.Smartphone),
        MenuItem("News", Icons.Default.Newspaper),
        MenuItem("Check Weather", Icons.Default.WbSunny),
        MenuItem("Contact Us", Icons.Default.Call),
        MenuItem("Settings", Icons.Default.Settings)
    )

    // Using Scaffold for a consistent screen structure
    Scaffold(
        topBar = {
            // Reusing the existing TopAppBar
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "All Menu",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Go back
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        // LazyVerticalGrid creates a scrollable 2-column grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(menuItems) { item ->
                MenuItemCard(menuItem = item, onClick = {
                    // TODO: Handle navigation for each item
                    println("${item.title} clicked")
                })
            }
        }
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // Makes the card a square
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)), // Light green color
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                modifier = Modifier.size(48.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = menuItem.title,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllMenuScreenPreview() {
    AllMenuScreen(navController = rememberNavController())
}