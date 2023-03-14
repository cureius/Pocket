package com.cureius.pocket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = darkColors(
    primary = Indigo500,
    primaryVariant = Indigo500,
    secondary = TomatoRed,

//     Other default colors to override
    background = Gray200,
    surface = Gray300,
    onPrimary = White,
    onSecondary = Gray800,
    onBackground = Black,
    onSurface = Gray900,
)

private val DarkColorPalette = lightColors(
    primary = BlueA200,
    primaryVariant = BlueA200,
    secondary = Orange,

//     Other default colors to override
    background = Black,
    surface = Gray800,
    onPrimary = Gray900,
    onSecondary = Gray900,
    onBackground = Gray300,
    onSurface = Gray500,
)

@Composable
fun PocketTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}