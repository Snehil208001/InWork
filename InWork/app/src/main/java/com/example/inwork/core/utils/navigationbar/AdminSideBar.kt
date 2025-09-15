package com.example.inwork.core.utils.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
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
    onSendNoticeClick: () -> Unit
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
                NavigationMenuItem(icon = Icons.Default.PersonAdd, text = "Add Employee")
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