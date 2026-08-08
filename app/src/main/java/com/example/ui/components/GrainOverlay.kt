package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun GrainOverlay(
    modifier: Modifier = Modifier,
    opacity: Float = 0.08f
) {
    // Generate static noise point cloud pattern
    val density = LocalDensity.current
    val noisePoints = remember {
        val points = mutableListOf<androidx.compose.ui.geometry.Offset>()
        val rand = Random(42)
        val numPoints = 12000
        for (i in 0 until numPoints) {
            points.add(
                androidx.compose.ui.geometry.Offset(
                    x = rand.nextFloat(),
                    y = rand.nextFloat()
                )
            )
        }
        points
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val mappedPoints = noisePoints.map { point ->
            androidx.compose.ui.geometry.Offset(
                x = point.x * width,
                y = point.y * height
            )
        }

        drawPoints(
            points = mappedPoints,
            pointMode = PointMode.Points,
            color = Color.White.copy(alpha = opacity),
            strokeWidth = 1.5f
        )
    }
}
