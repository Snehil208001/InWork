package com.example.inwork.mainui.splashscreen.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.splashscreen.R.drawable.logopreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    val offsetY = remember { Animatable(-400f) }   // Start high
    val offsetX = remember { Animatable(0f) }      // Start center
    val rotation = remember { Animatable(0f) }     // Rotation

    LaunchedEffect(true) {
        coroutineScope {
            // Y drop - with smooth easing
            launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 1500
                        -400f at 0 using FastOutSlowInEasing     // start high with smooth easing
                        80f at 700 using FastOutSlowInEasing     // reduced overshoot with smooth easing
                        0f at 1200 using FastOutSlowInEasing     // final position with smooth easing
                    }
                )
            }

            // X swing - with smooth easing
            launch {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 1500
                        0f at 0 using FastOutSlowInEasing         // start center with smooth easing
                        90f at 300 using FastOutSlowInEasing     // early peak with smooth easing
                        80f at 500 using FastOutSlowInEasing      // hold at peak with smooth easing
                        0f at 1200 using FastOutSlowInEasing      // return to center with smooth easing
                    }
                )
            }

            // Rotation with slower speed to match
            launch {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(durationMillis = 2500, easing = FastOutSlowInEasing)
                )
            }
        }

        // In a real app, navigation happens after the animation.
        // For preview purposes, we can comment this out or handle it gracefully.
        // In a live run, this will navigate.
        if (navController.currentBackStackEntry?.destination?.route != "Splash") {
            navController.navigate("login") {
                popUpTo("Splash") { inclusive = true }
            }
        }
    }

    // --- UI Layout ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo dropping with curve
            Image(
                painter = painterResource(id = logopreview),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = offsetX.value.dp, y = offsetY.value.dp)
                    .rotate(rotation.value)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Inwork: Connect. Collaborate. Succeed.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = "Design by INVYU",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // We use rememberNavController() for the preview, as it doesn't have a real navigation graph.
    SplashScreen(navController = rememberNavController())
}