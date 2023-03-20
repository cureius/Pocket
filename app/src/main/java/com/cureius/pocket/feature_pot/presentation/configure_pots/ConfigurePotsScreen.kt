package com.cureius.pocket.feature_pot.presentation.configure_pots

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.configure_pots.components.RangeSlider
import kotlin.math.roundToInt

@Preview
@Composable
fun ConfigurePotsScreen(viewModel: ConfigurePotsViewModel = hiltViewModel()) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var weightList = viewModel.nodes.value
    var nodeList = mutableListOf<MutableState<Float>>()
    var potTemplateList = viewModel.state.value
    weightList.add(0, remember {
        mutableStateOf(0.0f)
    })
    weightList.forEachIndexed { index, mutableState ->
        if (mutableState != null) {
            nodeList.add(index, remember {
                mutableStateOf(
                    mutableState.value + if (index > 0) {
                        nodeList[index - 1].value
                    } else {
                        0.0f
                    }
                )
            })
        }
    }
    println(nodeList)
    Log.d("nodes", "ConfigurePotsScreen: $nodeList")
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
                        value = text,
                        label = { Text(text = "Monthly Income") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { it ->
                            text = it
                        },
                    )
                    Card(
                        elevation = 4.dp, shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primary.copy(
                                        alpha = 0.6f
                                    )
                                )
                                .padding(8.dp)
                                .clickable {}, contentAlignment = Alignment.Center
                        ) {
                            val config = ImageVector.vectorResource(id = R.drawable.sliders)
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "config",
                                tint = MaterialTheme.colors.background,
                            )
                        }
                    }
                }

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                itemsIndexed(potTemplateList) { ix, pot ->
                    ConfigurablePotItem(
                        initialStart = nodeList[ix].value,
                        initialEnd = nodeList[ix + 1].value,
                        onStartChange = {
                            nodeList[ix].value = it
                        },
                        onEndChange = {
                            nodeList[ix + 1].value = it
                        },
                        capacity = pot.capacity ?: 0.0,
                        label = pot.title,
                        icon = pot.icon,
                        filled = pot.filled ?: 0.0f,
                        weight = pot.weight ?: 0.3f
                    )
                }
            }
        }
    }
}

@Composable
fun ConfigurablePotItem(
    initialStart: Float = 0.0f,
    initialEnd: Float = 0.0f,
    onStartChange: (Float) -> Unit,
    onEndChange: (Float) -> Unit,
    label: String,
    icon: String?,
    filled: Float,
    capacity: Double,
    weight: Float
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
                        Row() {
                            Text(
                                text = "${(capacity * filled).roundToInt()}/${capacity.roundToInt()}",
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Normal),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp)
                            )
                            Text(
                                text = " ${(filled * 100).roundToInt()}%",
                                color = MaterialTheme.colors.onSecondary,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        MaterialTheme.colors.secondary, RoundedCornerShape(4.dp)
                                    )
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
                ) {
                    RangeSlider(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                        rangeColor = Color(73, 147, 236),
                        backColor = Color(203, 225, 246),
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
                        })
                }
            }

        }

    }
}
