package com.cureius.pocket.feature_pot.presentation.configure_pots.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        Text(
            text = label,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(0.dp, 0.dp)
        )
    }
}
