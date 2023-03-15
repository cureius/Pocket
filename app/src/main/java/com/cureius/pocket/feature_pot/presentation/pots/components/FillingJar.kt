package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FillingJar(percentageFull: Int) {
    val animatedWaterHeight = remember { Animatable(0f) }
    val jarColor = Color.Transparent
    val waterColor = MaterialTheme.colors.primary.copy(alpha = 0.7f)
    // Animate the water height
    LaunchedEffect(percentageFull) {
        animatedWaterHeight.animateTo(100 * (percentageFull / 100f))
    }
    Canvas(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(0.3f)
            .padding(4.dp)
    ) {
        val waterHeight = size.height * (percentageFull / 100f)

        // Draw the jar
        drawRect(
            color = jarColor,
            size = Size(size.width * 0.8f, size.height * 0.8f),
            topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
            style = Stroke(4.dp.toPx())
        )

        // Draw the static water
        drawRect(
            color = waterColor,
            size = Size(size.width * 0.8f, waterHeight),
            topLeft = Offset(size.width * 0.1f, size.height * 0.9f - waterHeight),
        )

        // Draw the animated water
        drawRect(
            color = waterColor,
            size = Size(size.width * 0.8f, animatedWaterHeight.value),
            topLeft = Offset(size.width * 0.1f, size.height * 0.9f - animatedWaterHeight.value),
        )
    }
}


@Preview
@Composable
fun waterJar() {
    FillingJar(20)
}
