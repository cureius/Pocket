package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LeftConcaveMask(modifier: Modifier = Modifier,color: Color = Color.Black) {
    Canvas(
        modifier = modifier,
    ) {
        val width = size.width
        val height = size.height

        // draw jar body
        drawPath(
            path = Path().apply {
                moveTo(width * 0.0f, height * 0.0f)
                lineTo(width * 0.0f, height * 1.0f)

                arcTo(
                    Rect(
                        left = width * 0.0f,
                        top = height * 0.0f,
                        right = width * 2.0f,
                        bottom = height * 2.0f
                    ), 180f, 90f, false
                )
                lineTo(width * 0.0f, height * 0.0f)
                close()
            }, color = color,  style = Fill
        )
    }
}