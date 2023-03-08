package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R

@Preview
@Composable
fun InfoSection() {
    val shape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(136.dp)
            .padding(16.dp, 0.dp, 16.dp, 0.dp),
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary.copy(alpha = 0.2f), shape)
                .fillMaxWidth()
                .padding(8.dp)
                .height(104.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp, 8.dp, 4.dp, 12.dp)
                            .fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Balance")
                        Text(text = "$5,000.00", color = MaterialTheme.colors.primaryVariant)
                    }
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        drawLine(
                            color = Color.DarkGray.copy(alpha = 0.5f),
                            start = Offset(x = 0f, y = size.height / 2),
                            end = Offset(x = size.width * 0.95f, y = size.height / 2),
                            strokeWidth = 8f,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(20f, 10f),
                                phase = 0f
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$ 5,000.00", color = MaterialTheme.colors.primaryVariant)
                            Text(text = "Income")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$ 5,000.00", color = MaterialTheme.colors.primaryVariant)
                            Text(text = "Spent")
                        }

                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                            ){
                        val pngImage: Painter = painterResource(id = R.drawable.sc2)
                        Image(
                            painter = pngImage,
                            contentDescription = "Scan To Pay",
                            modifier = Modifier
                                .size(92.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary)
                        )
                        Text(
                            text = "Scan To Pay",
                            color = MaterialTheme.colors.onPrimary,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 10.sp
                        )
                    }

                }
            }
        }
    }
}