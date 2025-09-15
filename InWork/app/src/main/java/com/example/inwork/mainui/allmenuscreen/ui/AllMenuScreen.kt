package com.example.inwork.mainui.adminhomescreen.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.mainui.allmenuscreen.viewmodel.AllMenuViewModel
import com.example.inwork.mainui.allmenuscreen.viewmodel.MenuItem


/**
 * The main content composable that builds the entire multi-section menu screen.
 * It is now stateless and driven by the AllMenuViewModel.
 */
@Composable
fun AllMenuContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AllMenuViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        // "General" Section
        item {
            SectionHeader("General")
        }
        item {
            MenuRow(items = state.generalItems)
        }


        // Placeholder Banner
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalBannerPlaceholder()
        }


        // "Management" Section
        item {
            SectionHeader("Management Tools")
        }
        items(state.managementItems.chunked(4)) { rowItems ->
            MenuRow(items = rowItems)
        }


        // "Communication" Section
        item {
            SectionHeader("Communication")
        }
        item {
            MenuRow(items = state.communicationItems)
        }


        // "Support" Section
        item {
            SectionHeader("Support")
        }
        item {
            MenuRow(items = state.supportItems)
        }
    }
}


/**
 * A composable for creating a consistent section header.
 */
@Composable
fun SectionHeader(title: String) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


/**
 * A composable that creates a row of menu items, adding spacers if needed.
 */
@Composable
fun MenuRow(items: List<MenuItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (item in items) {
            NewMenuItemCard(
                modifier = Modifier.weight(1f),
                menuItem = item,
                onClick = { println("${item.title} clicked") }
            )
        }
        // Add spacers to align rows with fewer than 4 items
        if (items.size < 4) {
            for (i in 0 until (4 - items.size)) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}




/**
 * A placeholder for the large horizontal banner, themed with your app's color.
 */
@Composable
fun HorizontalBannerPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)) // Themed green color
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Company Updates",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Updates",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}




/**
 * The composable for the individual menu item card, themed with your app's color.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMenuItemCard(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Ensures the card is square
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                modifier = Modifier.size(40.dp),
                tint = Color.Black.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = menuItem.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllMenuContentPreview() {
    AllMenuContent(navController = rememberNavController())
}