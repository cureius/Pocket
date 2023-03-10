package com.cureius.pocket.feature_account.presentation.account.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CharBoxView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier
            .width(28.dp)
            .border(
                1.dp, when {
                    isFocused -> MaterialTheme.colors.onBackground
                    else -> MaterialTheme.colors.onBackground
                }, RoundedCornerShape(8.dp)
            )
            .padding(2.dp),
        text = char,
        color = if (isFocused) {
            MaterialTheme.colors.onSurface
        } else {
            MaterialTheme.colors.onBackground
        },
        textAlign = TextAlign.Center,
        style = TextStyle(fontWeight = FontWeight.Bold),
        fontSize = 20.sp,
        maxLines = 1,
    )
}
