package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IncomeCreditPrompt(){
    val buttonShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    val wrapperShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                MaterialTheme.colors.primary.copy(alpha = 0.8f),
                wrapperShape,
            )
            .border(
                2.dp,
                MaterialTheme.colors.primary.copy(alpha = 0.4f),
                wrapperShape
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.6f)) {
                Text(
                    text = "Detected A Credited Amount",
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 12.sp
                )
                Text(
                    text = "Press To Consider As INCOME",
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow.copy(alpha = 0.9f), buttonShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$1,80,000.00",
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 18.sp
                )
            }
        }

    }
    Spacer(modifier = Modifier.height(16.dp))
}