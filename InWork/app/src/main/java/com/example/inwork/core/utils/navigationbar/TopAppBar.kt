package com.example.inwork.core.utils.navigationbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inwork.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InWorkTopAppBar(
    onNavigationIconClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Home",
                // CHANGED: Ensured font weight is bold and increased size slightly
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Drawer",
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.Black, // Border color from image
                            shape = CircleShape
                        )
                        .padding(8.dp)
                        .size(28.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle profile click */ }) {
                Image(
                    painter = painterResource(id = R.drawable.logopreview),
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        )
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            // CHANGED: More vibrant green background
            containerColor = Color(0xFFC8E6C9)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun InWorkTopAppBarPreview() {
    InWorkTopAppBar(onNavigationIconClick = {})
}