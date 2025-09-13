package com.example.inwork.mainui.splashscreen.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.inwork.R
import com.example.inwork.core.navigation.Routes // Import your Routes object
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    val offsetY = remember { Animatable(-400f) }
    val offsetX = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val context = LocalContext.current

    LaunchedEffect(true) {
        coroutineScope {
            launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 1500
                        -400f at 0 using FastOutSlowInEasing
                        80f at 700 using FastOutSlowInEasing
                        0f at 1200 using FastOutSlowInEasing
                    }
                )
            }

            launch {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 1500
                        0f at 0 using FastOutSlowInEasing
                        90f at 300 using FastOutSlowInEasing
                        80f at 500 using FastOutSlowInEasing
                        0f at 1200 using FastOutSlowInEasing
                    }
                )
            }

            launch {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(durationMillis = 2500, easing = FastOutSlowInEasing)
                )
            }
        }

        val hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val destination = if (hasLocationPermission) {
            Routes.onboarding
        } else {
            Routes.permission
        }

        navController.navigate(destination) {
            popUpTo(Routes.splash) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Always use a white background
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopreview),
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

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}