package com.cureius.pocket.feature_pot.presentation.pots.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Chart(
    data: Map<String, Float>, maxValue: Double?, modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    // BarGraph Dimensions
    val barGraphWidth by remember { mutableStateOf(10.dp) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1.0f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        // graph
        data.forEach {
            Column(
                modifier = Modifier
                    .padding(start = barGraphWidth / 2, end = barGraphWidth / 2)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colors.surface, RoundedCornerShape(CornerSize(12.dp))
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(CornerSize(4.dp)))
                    .width(barGraphWidth)
                    .fillMaxHeight(it.value * 0.9f)
                    .background(MaterialTheme.colors.primary.copy(alpha = 1.0f))
                    .clickable {
                        Toast
                            .makeText(context, it.value.toString(), Toast.LENGTH_SHORT)
                            .show()
                    })
                Text(
                    color = MaterialTheme.colors.onSurface,
                    text = it.key.uppercase(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }

        // scale Y-Axis
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            maxValue?.toString()?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(2.dp, 0.dp)
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.8f))
        }
    }
}

@Preview
@Composable
fun ChartPreview() {
    Chart(
        data = mapOf(
            Pair("mo", 0.5f),
            Pair("tu", 0.1f),
            Pair("we", 0.6f),
            Pair("th", 0.2f),
            Pair("fr", 0.3f),
            Pair("sa", 0.7f),
            Pair("su", 0.8f),
        ), maxValue = 100.0, modifier = Modifier
            .fillMaxSize()
            .offset(y = (12).dp)
    )
}






