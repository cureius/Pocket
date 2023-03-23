package com.cureius.pocket.feature_account.presentation.add_account.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun OutlineTextBox(
    content: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .border(
                1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        text = content,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
        textAlign = TextAlign.Center,
        style = TextStyle(fontWeight = FontWeight.Light),
        fontSize = 20.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}