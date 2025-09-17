package com.example.inwork.core.utils.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inwork.R

@Composable
fun AdminSideBar(
    companyName: String,
    email: String,
    onAddEventClick: () -> Unit,
    onSendNoticeClick: () -> Unit,
    onAddEmployeeClick: () -> Unit // Added this parameter
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopreview),
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = companyName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Divider(color = Color.LightGray)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))
        ) {
            item {
                NavigationMenuItem(icon = Icons.Default.Home, text = "Home")
                NavigationMenuItem(icon = Icons.Default.Person, text = "Profile")
                NavigationMenuItem(icon = Icons.Default.Groups, text = "All Employees")
                NavigationMenuItem(icon = Icons.Default.Send, text = "Sent Notices")
                NavigationMenuItem(icon = Icons.Default.Event, text = "Add Event", onClick = onAddEventClick)
            }

            item { NavigationHeader(text = "Manage Employees") }
            item {
                NavigationMenuItem(icon = Icons.Default.Work, text = "Employee Status")
                NavigationMenuItem(icon = Icons.Default.CalendarToday, text = "Leave Requests")
                NavigationMenuItem(icon = Icons.Default.Assessment, text = "Monthly Reports")
                NavigationMenuItem(icon = Icons.Default.Send, text = "Send Notice", onClick = onSendNoticeClick)
                // Updated this item to be clickable
                NavigationMenuItem(icon = Icons.Default.PersonAdd, text = "Add Employee", onClick = onAddEmployeeClick)
                NavigationMenuItem(icon = Icons.Default.Business, text = "Add Offices")
                NavigationMenuItem(icon = Icons.Default.LocationCity, text = "All Offices")
                NavigationMenuItem(icon = Icons.Default.Dashboard, text = "Dashboard")
                NavigationMenuItem(icon = Icons.Default.Smartphone, text = "Screen Time Details")
                NavigationMenuItem(icon = Icons.Default.Newspaper, text = "Memes, Jokes and News")
                NavigationMenuItem(icon = Icons.Default.WbSunny, text = "Check Weather")
            }
        }
    }
}