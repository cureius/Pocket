package com.cureius.pocket.util.components


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

@Composable
fun MonthPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    showReset: Boolean = false,
    confirmButtonCLicked: (Int, Int) -> Unit,
    cancelClicked: () -> Unit,
    resetClicked: () -> Unit
) {

    val months = listOf(
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    )

    var month by remember {
        mutableStateOf(months[currentMonth - 1])
    }

    var year by remember {
        mutableStateOf(currentYear)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    if (visible) {

        AlertDialog(backgroundColor = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(15.dp),
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(90f)
                                .clickable(indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year--
                                    }),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = year.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onBackground
                        )

                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(-90f)
                                .clickable(indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year++
                                    }),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(), elevation = 0.dp
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisSpacing = 8.dp,
                            crossAxisSpacing = 8.dp,
                            mainAxisAlignment = MainAxisAlignment.Center,
                            crossAxisAlignment = FlowCrossAxisAlignment.Center
                        ) {

                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                month = it
                                            })
                                        .background(
                                            color = Color.Transparent
                                        ), contentAlignment = Alignment.Center
                                ) {

                                    val animatedSize by animateDpAsState(
                                        targetValue = if (month == it) 60.dp else 0.dp,
                                        animationSpec = tween(
                                            durationMillis = 200, easing = LinearOutSlowInEasing
                                        ),
                                        label = "Month Animation"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                color = if (month == it) MaterialTheme.colors.primary.copy(
                                                    0.5f
                                                ) else Color.Transparent, shape = CircleShape
                                            )
                                    )

                                    Text(
                                        text = it,
                                        color = if (month == it) Color.White else MaterialTheme.colors.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )

                                }
                            }

                        }

                    }

                }

            },
            buttons = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Rounded Button
                    if (!showReset) OutlinedButton(
                        onClick = {
                            cancelClicked()
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                    if (showReset) OutlinedButton(
                        onClick = {
                            resetClicked()
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Reset")
                    }
                    Button(
                        onClick = {
                            confirmButtonCLicked(
                                months.indexOf(month) + 1, year
                            )
                        },
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Confirm")
                    }
                }
            },
            onDismissRequest = {

            })

    }

}

@Composable
@Preview
fun MonthPickerPreview() {
    MonthPicker(visible = true,
        currentMonth = 1,
        currentYear = 2022,
        confirmButtonCLicked = { month, year -> },
        cancelClicked = { },
        resetClicked = { })
}









