package com.example.inwork.mainui.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inwork.mainui.dashboardscreen.viewmodel.DashboardViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel = viewModel()) {
    val uiState by dashboardViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CheckInOutChart(
            checkInData = uiState.checkInData,
            checkOutData = uiState.checkOutData,
            months = uiState.checkInOutMonths
        )
        Spacer(modifier = Modifier.height(24.dp))
        OthersActivityChart(chartData = uiState.activityChartData)
        Spacer(modifier = Modifier.height(24.dp))
        PerformanceChart(performanceData = uiState.performanceData)
    }
}

@Composable
fun CheckInOutChart(
    checkInData: List<Float>,
    checkOutData: List<Float>,
    months: List<String>
) {
    var startAnimation by remember { mutableStateOf(false) }
    val maxVal = 30f
    val barMaxHeight = 160.dp

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in maxVal.toInt() downTo 0 step 5) {
                        Text(text = i.toString(), fontSize = 12.sp)
                    }
                }

                months.forEachIndexed { index, month ->
                    val animInFraction by animateFloatAsState(
                        targetValue = if (startAnimation) (checkInData[index] / maxVal) else 0f,
                        animationSpec = tween(durationMillis = 900)
                    )
                    val animOutFraction by animateFloatAsState(
                        targetValue = if (startAnimation) (checkOutData[index] / maxVal) else 0f,
                        animationSpec = tween(durationMillis = 900)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(barMaxHeight)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                ) {
                                    Text(text = "${checkInData[index]}", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(barMaxHeight * animInFraction)
                                            .background(Color.Blue)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                ) {
                                    Text(text = "${checkOutData[index]}", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(barMaxHeight * animOutFraction)
                                            .background(Color.Red)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(month, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = Color.Blue, text = "CheckIn")
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(color = Color.Red, text = "CheckOut")
            }
        }
    }
}

@Composable
fun OthersActivityChart(chartData: Map<String, Float>) {
    val colors = listOf(Color.Red, Color.Blue, Color.Green)
    var startAnimation by remember { mutableStateOf(false) }

    val animatedSweepAngles = chartData.values.map {
        animateFloatAsState(
            targetValue = if (startAnimation) it * 360f else 0f,
            animationSpec = tween(durationMillis = 1000)
        ).value
    }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Others Activity :",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 85f
                    var startAngle = -90f

                    animatedSweepAngles.forEachIndexed { index, sweepAngle ->
                        drawArc(
                            color = colors[index],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle - 2,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                        startAngle += sweepAngle
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                chartData.keys.forEachIndexed { index, label ->
                    LegendItem(color = colors[index], text = label, useColoredText = true)
                    if (index < chartData.size - 1) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PerformanceChart(performanceData: List<Pair<Float, Float>>) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 1500))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Performance :",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LineChart(
                data = performanceData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                animationProgress = animationProgress.value
            )
            Spacer(modifier = Modifier.height(8.dp))
            LegendItem(color = Color.Green, text = "Progress")
        }
    }
}

@Composable
fun LineChart(data: List<Pair<Float, Float>>, modifier: Modifier = Modifier, animationProgress: Float) {
    val density = LocalDensity.current
    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = with(density) { 12.sp.toPx() }
            color = android.graphics.Color.DKGRAY
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    val yAxisPadding = 30.dp
    val xAxisPadding = 20.dp

    Canvas(modifier = modifier) {
        val yAxisPaddingPx = yAxisPadding.toPx()
        val xAxisPaddingPx = xAxisPadding.toPx()

        val chartWidth = size.width - yAxisPaddingPx
        val chartHeight = size.height - xAxisPaddingPx

        val xMin = 1.2f; val xMax = 2.7f
        val yMin = 100f; val yMax = 110f

        for (i in 0..5) {
            val y = chartHeight - (i * chartHeight / 5)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(yAxisPaddingPx, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
            val label = yMin + (i * (yMax - yMin) / 5)
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    String.format("%.0f", label),
                    yAxisPaddingPx / 2,
                    y + textPaint.textSize / 2,
                    textPaint
                )
            }
        }

        val xLabels = listOf(1.2f, 1.5f, 1.8f, 2.1f, 2.4f, 2.7f)
        xLabels.forEach { label ->
            val x = yAxisPaddingPx + ((label - xMin) / (xMax - xMin)) * chartWidth
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(x, 0f),
                end = Offset(x, chartHeight),
                strokeWidth = 1.dp.toPx()
            )
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    label.toString(),
                    x,
                    size.height - xAxisPaddingPx / 2 + textPaint.textSize / 2,
                    textPaint
                )
            }
        }

        val path = Path()
        data.forEachIndexed { index, (x, y) ->
            val pointX = yAxisPaddingPx + ((x - xMin) / (xMax - xMin)) * chartWidth
            val pointY = chartHeight - ((y - yMin) / (yMax - yMin)) * chartHeight
            if (index == 0) path.moveTo(pointX, pointY) else path.lineTo(pointX, pointY)
        }

        val pathMeasure = PathMeasure(); pathMeasure.setPath(path, false)
        val animatedPath = Path()
        pathMeasure.getSegment(0f, pathMeasure.length * animationProgress, animatedPath, true)
        drawPath(path = animatedPath, color = Color.Green, style = Stroke(width = 2.dp.toPx()))

        data.forEach { (x, y) ->
            val pointX = yAxisPaddingPx + ((x - xMin) / (xMax - xMin)) * chartWidth
            val pointY = chartHeight - ((y - yMin) / (yMax - yMin)) * chartHeight
            drawCircle(Color.Green, radius = 4.dp.toPx(), center = Offset(pointX, pointY))
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String, useColoredText: Boolean = false) {
    if (useColoredText) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = color)
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(14.dp).background(color))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}