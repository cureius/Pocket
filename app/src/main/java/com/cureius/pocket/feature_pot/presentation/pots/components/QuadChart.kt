package com.cureius.pocket.feature_pot.presentation.pots.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun QuadChart(
    data: List<Pair<Int, Double>> = emptyList(),
    graphCustomColor: Color = MaterialTheme.colors.primary,
    labelColor: Color = MaterialTheme.colors.onSurface,
    modifier: Modifier = Modifier,
) {
    val spacing = 10f
    val graphColor = graphCustomColor.copy(alpha = 0.5f)
    val transparentGraphColor = remember { graphColor.copy(alpha = 0.5f) }
    val upperValue = remember { (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = labelColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val bottomTextPaint = remember(density) {
        Paint().apply {
            color = labelColor.copy(alpha = 0.5f).toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        val spacePerHour = (size.width - spacing) / data.size
        (data.indices step 6).forEach { i ->
            val hour = data[i].first
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(), spacing + i * spacePerHour, size.height, bottomTextPaint
                )
            }
        }

        val priceStep = (upperValue - lowerValue) / 5f
        drawContext.canvas.nativeCanvas.apply {
            drawTextOnRight(
                text = round((lowerValue + priceStep * 5) - 1).toString(),
                x = size.width, // Adjust the value as needed
                y = size.height - spacing - 4 * size.height / 5f,
                textPaint = textPaint
            )
        }

        var medX: Float
        var medY: Float
        val strokePath = Path().apply {
            val height = size.height - 20f
            data.indices.forEach { i ->
                val nextInfo = data.getOrNull(i + 1) ?: data.last()
                val firstRatio = (data[i].second - lowerValue) / (upperValue - lowerValue)
                val secondRatio = (nextInfo.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (firstRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (secondRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                } else {
                    medX = (x1 + x2) / 2f
                    medY = (y1 + y2) / 2f
                    quadraticBezierTo(x1 = x1, y1 = y1, x2 = medX, y2 = medY)
                }
            }
        }

        drawPath(
            path = strokePath, color = graphCustomColor, style = Stroke(
                width = 3.dp.toPx(), cap = StrokeCap.Butt
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerHour, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath, brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor, Color.Transparent
                ), endY = size.height - spacing
            )
        )

    }
}

@Composable
@Preview
fun QuadChartPreview() {
    QuadChart(
        data = listOf(
            Pair(1, 0.45),
            Pair(2, 0.0),
            Pair(3, 10000.45),
            Pair(4, 112.25),
            Pair(5, 116.45),
            Pair(6, 113.35),
            Pair(7, 118.65),
            Pair(8, 110.15),
            Pair(9, 113.05),
            Pair(10, 114.25),
            Pair(11, 0.35),
            Pair(12, 117.45),
            Pair(13, 112.65),
            Pair(14, 115.45),
            Pair(15, 111.85),
            Pair(16, 111.45),
            Pair(17, 111.0),
            Pair(18, 0.45),
            Pair(19, 112.25),
            Pair(20, 116.45),
            Pair(21, 113.35),
            Pair(22, 0.65),
            Pair(23, 110.15),
            Pair(24, 113.05),
            Pair(25, 114.25),
            Pair(26, 116.35),
            Pair(27, 117.45),
            Pair(28, 112.65),
            Pair(29, 115.45),
            Pair(30, 111.85),
            Pair(31, 0.45),
        )
    )
}

fun DrawScope.drawTextOnRight(
    text: String,
    x: Float,
    y: Float,
    textPaint: Paint
) {
    val textWidth = textPaint.measureText(text)
    val startX = x - textWidth
    drawContext.canvas.nativeCanvas.drawText(text, startX, y, textPaint)
}