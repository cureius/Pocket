package com.cureius.pocket.feature_pot.presentation.configure_pots.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import kotlin.math.roundToInt

@Composable
fun ConfigurablePotItem(
    initialStart: Float = 0.0f,
    initialEnd: Float = 0.0f,
    label: String,
    icon: String?,
    totalCapacity: String,
    onStartChange: (Float) -> Unit,
    onEndChange: (Float) -> Unit,
) {
    val paddingModifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(144.dp)
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 0.dp,
        modifier = paddingModifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column {
                var weight = (initialEnd.minus(initialStart).toString())
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    MaterialTheme.colors.secondary.copy(0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                        ) {
                            val icon =
                                IconDictionary.allIcons[icon]?.let { ImageVector.vectorResource(id = it) }
                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "add account",
                                    tint = MaterialTheme.colors.onBackground,
                                )
                            }
                        }
                        Text(
                            text = label,
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                            .widthIn(max = 130.dp, min = 0.dp), // Adjust the minimum width as per your requirement

                        )
                    }

                    Box(
                        modifier = Modifier.background(
                            MaterialTheme.colors.primaryVariant.copy(0.1f), RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.background(
                                Color.Transparent,
                            )
                        ) {
                            Box(
                                modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BasicTextField(
                                    value = if (totalCapacity.isEmpty()) {
                                        "Allocated"
                                    } else {
                                        "${
                                            (totalCapacity.toDouble() * weight.toFloat()).roundToInt()
                                        }/$totalCapacity"

                                    },
                                    onValueChange = { weight = it },
                                    readOnly = true,
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colors.onBackground
                                    ).copy(
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .align(Alignment.CenterEnd),
                                    maxLines = 1
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .padding(4.dp, 4.dp, 0.dp, 4.dp)
                                    .background(
                                        MaterialTheme.colors.secondary.copy(0.1f),
                                        RoundedCornerShape(4.dp)
                                    )
                            ) {
                                BasicTextField(
                                    value = (weight.toFloat() * 100).roundToInt().toString(),
                                    onValueChange = { weight = it },
                                    readOnly = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.onBackground
                                    ).copy(
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(25.dp)
                                        .align(Alignment.Center),
                                    singleLine = true,
                                    maxLines = 1
                                )
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(0.dp, 4.dp, 8.dp, 4.dp)
                            ) {
                                Text(
                                    text = "%",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colors.onBackground
                                    ).copy(
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp, top = 4.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    RangeSlider(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        rangeColor = MaterialTheme.colors.primary.copy(alpha = 0.8f),
                        backColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                        startKnob = MaterialTheme.colors.secondary.copy(alpha = 1f),
                        endKnob = MaterialTheme.colors.primary.copy(alpha = 1f),
                        barHeight = 8.dp,
                        circleRadius = 10.dp,
                        progress1InitialValue = initialStart,
                        progress2InitialValue = initialEnd,
                        tooltipSpacing = 10.dp,
                        tooltipWidth = 40.dp,
                        tooltipHeight = 30.dp,
                        cornerRadius = CornerRadius(32f, 32f),
                        tooltipTriangleSize = 8.dp,
                        onProgressChanged = { progress1, progress2 ->
                            onStartChange(progress1)
                            onEndChange(progress2)
                            weight = (progress2.minus(progress1).toString())
                        })
                }
            }
        }
    }
}

@Composable
@Preview
fun ConfigurablePotItemPreview() {
    ConfigurablePotItem(
        label = "Emergency Fund",
        icon = "invest",
        totalCapacity = "",
        onStartChange = {},
        onEndChange = {}
    )
}
