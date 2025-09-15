package com.example.inwork.core.utils.navigationbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inwork.mainui.userhomescreen.ui.UserScreen

@Composable
fun UserBottomAppBar(
    currentScreen: UserScreen,
    onScreenSelected: (UserScreen) -> Unit
) {
    BottomAppBar(
        modifier = Modifier.clip(BottomAppBarShape()),
        containerColor = Color(0xFFC8E6C9),
        contentColor = Color.Black,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val unselectedColor = Color.DarkGray

            NavigationBarItem(
                selected = currentScreen == UserScreen.Home,
                onClick = { onScreenSelected(UserScreen.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(28.dp)) },
                label = { Text("Home", fontSize = 12.sp, fontWeight = if (currentScreen == UserScreen.Home) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = unselectedColor, selectedTextColor = Color.Black, unselectedTextColor = unselectedColor, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentScreen == UserScreen.AddGeo,
                onClick = { onScreenSelected(UserScreen.AddGeo) },
                icon = { Icon(Icons.Default.AddLocationAlt, contentDescription = "Add Geo", modifier = Modifier.size(28.dp)) },
                label = { Text("Add Geo", fontSize = 12.sp, fontWeight = if (currentScreen == UserScreen.AddGeo) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = unselectedColor, selectedTextColor = Color.Black, unselectedTextColor = unselectedColor, indicatorColor = Color.Transparent)
            )

            Spacer(modifier = Modifier.width(80.dp)) // Spacer for the FAB

            NavigationBarItem(
                selected = currentScreen == UserScreen.Notices,
                onClick = { onScreenSelected(UserScreen.Notices) },
                icon = { Icon(Icons.Default.Article, contentDescription = "Notices", modifier = Modifier.size(28.dp)) },
                label = { Text("Notices", fontSize = 12.sp, fontWeight = if (currentScreen == UserScreen.Notices) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = unselectedColor, selectedTextColor = Color.Black, unselectedTextColor = unselectedColor, indicatorColor = Color.Transparent)
            )
            NavigationBarItem(
                selected = currentScreen == UserScreen.Notification,
                onClick = { onScreenSelected(UserScreen.Notification) },
                icon = { Icon(Icons.Default.Notifications, contentDescription = "Notification", modifier = Modifier.size(28.dp)) },
                label = { Text("Notification", fontSize = 12.sp, fontWeight = if (currentScreen == UserScreen.Notification) FontWeight.Bold else FontWeight.Normal, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = unselectedColor, selectedTextColor = Color.Black, unselectedTextColor = unselectedColor, indicatorColor = Color.Transparent)
            )
        }
    }
}