package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    maxRange: Float = 100f,
    chartColors: List<Color> = listOf(Color.Blue),
    chartPrimaryAxisColor: Color = MaterialTheme.colors.secondary,
    chartSecondaryAxisColor: Color = MaterialTheme.colors.secondary,
    chartBorderColor: Color = MaterialTheme.colors.secondary,
    strokeWidth: Float = 2f
) {
    println(data)
    println(labels)
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            onDraw = {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val numPoints = data.size
                val anglePerIndex = (2 * PI) / numPoints

                // Draw the radar chart's axes
                drawAxes(
                    centerX,
                    centerY,
                    numPoints,
                    anglePerIndex,
                    chartPrimaryAxisColor,
                    chartSecondaryAxisColor
                )

                // Draw the data lines
                drawDataLines(
                    centerX,
                    centerY,
                    data,
                    numPoints,
                    anglePerIndex,
                    maxRange,
                    chartColors,
                    strokeWidth
                )

                drawBorderLines(
                    centerX,
                    centerY,
                    data,
                    numPoints,
                    anglePerIndex,
                    maxRange,
                    listOf(chartBorderColor),
                    strokeWidth
                )
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Display labels
            labels.forEach { label ->
                Text(text = label)
            }
        }
    }
}

private fun DrawScope.drawAxes(
    centerX: Float,
    centerY: Float,
    numPoints: Int,
    anglePerIndex: Double,
    primaryAxesColor: Color,
    secondaryAxesColor: Color
) {
    for (i in 0 until numPoints) {
        val angle = i * anglePerIndex
        val x = centerX + cos(angle).toFloat() * (size.width / 2)
        val y = centerY + sin(angle).toFloat() * (size.height / 2)
        drawLine(
            color = if (i == 0) {
                primaryAxesColor
            } else {
                secondaryAxesColor
            },
            start = Offset(centerX, centerY),
            end = Offset(x, y),
            strokeWidth =if (i == 0) {
                2f
            } else {
                1f
            } ,
            cap = Stroke.DefaultCap
        )
    }
}

private fun DrawScope.drawDataLines(
    centerX: Float,
    centerY: Float,
    data: List<Float>,
    numPoints: Int,
    anglePerIndex: Double,
    maxRange: Float,
    chartColors: List<Color>,
    strokeWidth: Float
) {
    if (data.size != numPoints) return

    val path = Path()
    val firstX = centerX + data[0] / maxRange * cos(0.0).toFloat() * (size.width / 2)
    val firstY = centerY + data[0] / maxRange * sin(0.0).toFloat() * (size.height / 2)
    path.moveTo(firstX, firstY)

    for (i in 1 until numPoints) {
        val angle = i * anglePerIndex
        val x = centerX + data[i] / maxRange * cos(angle).toFloat() * (size.width / 2)
        val y = centerY + data[i] / maxRange * sin(angle).toFloat() * (size.height / 2)
        path.lineTo(x, y)
    }

    path.close()
    drawPath(path, color = chartColors[0], style = Stroke(width = strokeWidth))
}


private fun DrawScope.drawBorderLines(
    centerX: Float,
    centerY: Float,
    data: List<Float>,
    numPoints: Int,
    anglePerIndex: Double,
    maxRange: Float,
    chartColors: List<Color>,
    strokeWidth: Float
) {
    if (data.size != numPoints) return

    val path = Path()
    val firstX = centerX + maxRange / maxRange * cos(0.0).toFloat() * (size.width / 2)
    val firstY = centerY + maxRange / maxRange * sin(0.0).toFloat() * (size.height / 2)
    path.moveTo(firstX, firstY)

    for (i in 1 until numPoints) {
        val angle = i * anglePerIndex
        val x = centerX + maxRange / maxRange * cos(angle).toFloat() * (size.width / 2)
        val y = centerY + maxRange / maxRange * sin(angle).toFloat() * (size.height / 2)
        path.lineTo(x, y)
    }

    path.close()
    drawPath(path, color = chartColors[0], style = Stroke(width = strokeWidth))
}

@Preview
@Composable
fun RadarChartPreview() {
    val data = listOf(90f, 60f, 40f, 70f, 90f)
    val labels = listOf("Label 1", "Label 2", "Label 3", "Label 4", "Label 5")

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            RadarChart(
                data = data,
                labels = labels,
                modifier = Modifier
                    .padding(16.dp)
                    .size(100.dp)
                    .background(Color.DarkGray),
                maxRange = 100f,
                chartColors = listOf(Color.Blue),
                strokeWidth = 3f
            )
        }
    }
}