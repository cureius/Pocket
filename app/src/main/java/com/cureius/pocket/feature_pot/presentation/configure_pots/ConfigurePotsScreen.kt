package com.cureius.pocket.feature_pot.presentation.configure_pots

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.feature_pot.presentation.configure_pots.components.ConfigurablePotItem
import com.cureius.pocket.feature_pot.presentation.configure_pots.components.CutCornerButton
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun ConfigurePotsScreen(viewModel: ConfigurePotsViewModel = hiltViewModel()) {
    val income = viewModel.income.value
    val nodeList = viewModel.nodes.value
    val potTemplateList = viewModel.state.value
    val nonMutableNodeList = nodeList.toList()
    Log.d("Nodes", "ConfigurePotsScreen: $nonMutableNodeList")
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
                            viewModel.onEvent(
                                ConfigurePotsEvent.UpdatePot(
                                    pot.id, it2.value.minus(it1.value)
                                )
                            )
                            ConfigurablePotItem(
                                initialStart = it1.value,
                                initialEnd = it2.value,
                                onStartChange = {
                                    if (potTemplateList.indexOf(pot) != 0) {
                                        val startNode =
                                            ((it * 100.0).roundToInt() / 100.0).toFloat()
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.RangeChange(
                                                ix, startNode
                                            )
                                        )
                                    }
                                },
                                onEndChange = {
                                    if (potTemplateList.indexOf(pot) != (potTemplateList.size - 1)) {
                                        val endNode = ((it * 100.0).roundToInt() / 100.0).toFloat()
                                        viewModel.onEvent(
                                            ConfigurePotsEvent.RangeChange(
                                                ix + 1, endNode
                                            )
                                        )
                                    }
                                },
                                totalCapacity = (income),
                                label = pot.title,
                                icon = pot.icon,
                            )
                        }
                    }
                }
                item {
                    CutCornerButton(
                        onClick = {
                            viewModel.onEvent(
                                ConfigurePotsEvent.SaveConfiguration
                            )
                        },
                        label = "Set Configuration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
