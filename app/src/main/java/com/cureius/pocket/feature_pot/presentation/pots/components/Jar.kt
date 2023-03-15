package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Jar(color: Color = Color.Black) {
    Canvas(
        modifier = Modifier.size(300.dp),
    ) {
        val width = size.width
        val height = size.height

        // draw jar body
        drawPath(
            path = Path().apply {
                moveTo(width * 0.33f, height * 0.1f)

                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.05f,
                        right = width * 0.35f,
                        bottom = height * 0.1f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.66f, height * 0.05f)
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.05f,
                        right = width * 0.70f,
                        bottom = height * 0.1f
                    ), -90f, 180f, false
                )

                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.1f,
                        right = width * 0.75f,
                        bottom = height * 0.2f
                    ), -90f, 160f, false
                )

                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.2f,
                        right = width * 0.80f,
                        bottom = height * 0.35f
                    ), -90f, 90f, false
                )

                lineTo(width * 0.80f, height * 0.92f)/*arcTo(
                    Rect(
                        left = width * 0.785f,
                        top = height * 0.28f,
                        right = width * 0.82f,
                        bottom = height * 0.92f
                    ), -90f, 180f, false
                )*/
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.85f,
                        right = width * 0.80f,
                        bottom = height * 0.99f
                    ), 0f, 90f, false
                )
                lineTo(width * 0.33f, height * 0.99f)
                arcTo(
                    Rect(
                        left = width * 0.20f,
                        top = height * 0.85f,
                        right = width * 0.35f,
                        bottom = height * 0.99f
                    ), 90f, 90f, false
                )
                lineTo(width * 0.2f, height * 0.3f)
                arcTo(
                    Rect(
                        left = width * 0.20f,
                        top = height * 0.2f,
                        right = width * 0.35f,
                        bottom = height * 0.35f
                    ), 180f, 90f, false
                )
                arcTo(
                    Rect(
                        left = width * 0.25f,
                        top = height * 0.10f,
                        right = width * 0.35f,
                        bottom = height * 0.2f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.33f, height * 0.1f)
                close()
            }, color = color, style = Stroke(width = 2.dp.toPx())
        )
        // draw jar lid
        drawPath(
            path = Path().apply {
                moveTo(width * 0.33f, height * 0.1f)
                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.05f,
                        right = width * 0.35f,
                        bottom = height * 0.1f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.66f, height * 0.05f)
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.05f,
                        right = width * 0.70f,
                        bottom = height * 0.1f
                    ), -90f, 180f, false
                )
            }, color = color, style = Fill
        )
        // draw jar top
        drawPath(
            path = Path().apply {
                moveTo(width * 0.25f, height * 0.2f)
                moveTo(width * 0.70f, height * 0.2f)
                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.10f,
                        right = width * 0.35f,
                        bottom = height * 0.2f
                    ), 90f, 90f, false
                )

            }, color = color, style = Stroke(width = 2.dp.toPx())
        )
    }
}