package com.cureius.pocket.feature_pot.presentation.configure_pots

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.configure_pots.components.RangeSlider
import kotlin.math.roundToInt

@Preview
@Composable
fun ConfigurePotsScreen(viewModel: ConfigurePotsViewModel = hiltViewModel()) {
    val income = viewModel.income.value
    val nodeList = viewModel.nodes.value
    val potTemplateList = viewModel.state.value

    Scaffold {
        Column {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(16.dp, 16.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = "Set Up Pots:",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextField(
                        value = income,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Monthly Income") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            viewModel.onEvent(ConfigurePotsEvent.EnteredIncome(it))
                        },
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                itemsIndexed(potTemplateList) { ix, pot ->
                    nodeList[ix]?.let { it1 ->
                        nodeList[ix + 1]?.let { it2 ->
                            nodeList[ix + 1]?.value?.minus(nodeList[ix]?.value!!)?.let { it3 ->
                                ConfigurablePotItem(
                                    initialStart = it1.value,
                                    initialEnd = it2.value,
                                    onStartChange = {
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.RangeChange(
                                                ix, it
                                            )
                                        )
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.UpdatePot(
                                                ix, it2.value.minus(it1.value)
                                            )
                                        )
                                    },
                                    onEndChange = {
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.RangeChange(
                                                ix + 1, it
                                            )
                                        )
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.UpdatePot(
                                                ix, it2.value.minus(it1.value)
                                            )
                                        )

                                    },
                                    totalCapacity = (income),
                                    label = pot.title,
                                    icon = pot.icon,
                                    filled = pot.filled ?: 0.0f,
                                    weight = it3.toFloat()
                                )
                            }
                        }
                    }
                }
                item {
                    ButtonWithCutCornerShape()
                }
            }
        }
    }
}

@Composable
fun ConfigurablePotItem(
    initialStart: Float = 0.0f,
    initialEnd: Float = 0.0f,
    label: String,
    icon: String?,
    weight: Float,
    filled: Float,
    totalCapacity: String,
    onStartChange: (Float) -> Unit,
    onEndChange: (Float) -> Unit,
) {
    val paddingModifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(140.dp)
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp,
        modifier = paddingModifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column {
                var weight = (initialEnd.minus(initialStart).toString())
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
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
                                    MaterialTheme.colors.secondary, RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                        ) {
                            val icon =
                                IconDictionary.allIcons[icon]?.let { ImageVector.vectorResource(id = it) }
                            if (icon != null) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "add account",
                                    tint = MaterialTheme.colors.surface,
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
                        )
                    }

                    Box(
                        modifier = Modifier.background(
                            MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.End, modifier = Modifier.background(
                                Color.Transparent
                            )
                        ) {
                            Log.d("Recompose", "ConfigurablePotItem: $weight")
                            Box(
                                modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BasicTextField(
                                    value = if (totalCapacity.isNullOrEmpty()) {
                                        "Percent Spent"
                                    } else {
                                        "${
                                            (totalCapacity.toDouble() * weight.toFloat()).roundToInt()
                                        }/$totalCapacity"

                                    },
                                    onValueChange = { weight = it },
                                    readOnly = true,
                                    textStyle = TextStyle(fontWeight = FontWeight.Bold).copy(
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
                                    .padding(4.dp)
                                    .background(
                                        MaterialTheme.colors.secondary, RoundedCornerShape(4.dp)
                                    )
                                    .width(36.dp), contentAlignment = Alignment.Center
                            ) {
                                BasicTextField(
                                    value = (weight.toFloat() * 100).roundToInt().toString() + "%",
                                    onValueChange = { weight = it },
                                    readOnly = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    textStyle = TextStyle(fontWeight = FontWeight.Bold).copy(
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .align(Alignment.Center),
                                    singleLine = true,
                                    maxLines = 1
                                )
                            }

                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize().padding(bottom = 20.dp , top = 4.dp), contentAlignment = Alignment.BottomCenter
                ) {
                    RangeSlider(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                        rangeColor = MaterialTheme.colors.primary,
                        backColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
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
fun ButtonWithCutCornerShape() {
    Button(
        onClick = {

        },
        shape = CutCornerShape(28),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "Confirm Configuration")
    }
}
