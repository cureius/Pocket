package com.cureius.pocket.feature_pot.presentation.configure_pots.components

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun CutCornerButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    shape: CutCornerShape = CutCornerShape(28)
) {
    Button(
        onClick = {
            onClick()
        },
        shape = shape,
        modifier = modifier
    ) {
        Text(text = label)
    }
}
