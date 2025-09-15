package com.example.inwork.mainui.adminhomescreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.core.utils.navigationbar.AdminBottomAppBar
import com.example.inwork.core.utils.navigationbar.AdminSideBar
import com.example.inwork.core.utils.navigationbar.InWorkTopAppBar
import kotlinx.coroutines.launch

@Composable
fun AdminHomeScreen(navController: NavController) {
    // 1. State for controlling the drawer (open/closed)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 2. Wrap the screen in a ModalNavigationDrawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 3. Provide the AdminSideBar as the drawer's content
            ModalDrawerSheet {
                AdminSideBar(
                    companyName = "Apple",
                    email = "soumadeepbarik@gmail.com"
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                // 4. Open the drawer when the menu icon is clicked
                InWorkTopAppBar(
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AdminBottomAppBar(navController)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Handle FAB click */ },
                    modifier = Modifier.offset(y = 60.dp),
                    shape = CircleShape,
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = "Admin Home Screen Content",
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AdminHomeScreenPreview() {
    AdminHomeScreen(navController = rememberNavController())
}