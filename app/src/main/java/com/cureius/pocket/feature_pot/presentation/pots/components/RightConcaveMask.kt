package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun RightConcaveMask(modifier: Modifier = Modifier, color: Color = Color.Black) {
    Canvas(
        modifier = modifier,
    ) {
        val width = size.width
        val height = size.height

        // draw jar body
        drawPath(
            path = Path().apply {
                moveTo(width * 1.0f, height * 0.0f)
                arcTo(
                    Rect(
                        left = width * -1.0f,
                        top = height * 0.0f,
                        right = width * 1.0f,
                        bottom = height * 2.0f
                    ), 0f, -90f, false
                )
                close()
            }, color = color,  style = Fill
        )
    }
}

@Preview
@Composable
fun RightConcaveMaskPreview(){
    RightConcaveMask(modifier= Modifier.size(300.dp))
}