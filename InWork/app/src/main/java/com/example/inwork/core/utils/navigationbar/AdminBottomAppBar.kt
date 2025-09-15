package com.example.inwork.core.utils.navigationbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.navigation.Screen

@Composable
fun AdminBottomAppBar(navController: NavController) {
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
                selected = true,
                onClick = { /* ... */ },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(28.dp)) },
                label = { Text("Home", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate(Screen.AllMenu.route)
                },
                icon = { Icon(Icons.Default.Apps, contentDescription = "All Menue", modifier = Modifier.size(28.dp)) },
                label = { Text("All Menue", fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(80.dp))

            NavigationBarItem(
                selected = false,
                onClick = { /* ... */ },
                icon = { Icon(Icons.Default.Article, contentDescription = "Sent Notice", modifier = Modifier.size(28.dp)) },
                label = { Text("Sent Notice", fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = false,
                onClick = { /* ... */ },
                icon = { Icon(Icons.Default.Notifications, contentDescription = "Notification", modifier = Modifier.size(28.dp)) },
                label = { Text("Notification", fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

class BottomAppBarShape(
    private val fabRadius: Dp = 36.dp,
    private val cornerRadius: Dp = 16.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val fabRadiusPx = with(density) { fabRadius.toPx() }
            val cornerRadiusPx = with(density) { cornerRadius.toPx() }
            val fabMarginPx = with(density) { 8.dp.toPx() }
            val cradleWidth = fabRadiusPx * 2 + fabMarginPx * 2
            val cradleHeight = fabRadiusPx + fabMarginPx
            val fabCenterX = size.width / 2

            moveTo(0f, cornerRadiusPx)
            arcTo(
                rect = Rect(Offset(0f, 0f), Size(cornerRadiusPx * 2, cornerRadiusPx * 2)),
                startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            lineTo(fabCenterX - cradleWidth / 2, 0f)
            cubicTo(
                x1 = fabCenterX - cradleWidth / 4, y1 = 0f,
                x2 = fabCenterX - fabRadiusPx, y2 = cradleHeight,
                x3 = fabCenterX, y3 = cradleHeight
            )
            cubicTo(
                x1 = fabCenterX + fabRadiusPx, y1 = cradleHeight,
                x2 = fabCenterX + cradleWidth / 4, y2 = 0f,
                x3 = fabCenterX + cradleWidth / 2, y3 = 0f
            )
            lineTo(size.width - cornerRadiusPx, 0f)
            arcTo(
                rect = Rect(Offset(size.width - cornerRadiusPx * 2, 0f), Size(cornerRadiusPx * 2, cornerRadiusPx * 2)),
                startAngleDegrees = 270f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminBottomAppBarPreview() {
    AdminBottomAppBar(navController = rememberNavController())
}