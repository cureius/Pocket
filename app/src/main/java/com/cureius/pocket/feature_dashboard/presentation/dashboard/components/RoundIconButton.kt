package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    iconColor: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 0.dp)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor
                )
            }
        }

        Text(text = label, style = MaterialTheme.typography.body2, color = MaterialTheme.colors.onBackground)
    }
}