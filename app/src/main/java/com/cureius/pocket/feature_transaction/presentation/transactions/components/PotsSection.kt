package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsEvent
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsViewModel

@Composable
fun PotsSection(
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    potsViewModel: PotsViewModel = hiltViewModel(),
    onOrderChange: () -> Unit
) {
    val potShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )
    var selectedPots = transactionViewModel.selectedPots.value
    val pots = potsViewModel.templatePots.value
    var localSelectedPots by remember { mutableStateOf(mutableListOf<Pot>()) }

    Column(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Pots",
                color = MaterialTheme.colors.onSurface,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 8.dp)
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items(pots) { item ->
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clickable(onClick = {
                            println("PotsSection ${item.title}")
                            if (selectedPots?.contains(item) == true && localSelectedPots.contains(item)) {
                                println("PotsSection ${item.title} removed")
                                transactionViewModel.onEvent(
                                    TransactionsEvent.RemovePots(item)
                                )
                                localSelectedPots.remove(item)
                            } else {
                                println("PotsSection ${item.title} added")
                                if (selectedPots != null) {
                                    transactionViewModel.onEvent(
                                        TransactionsEvent.SelectPots(
                                            item
                                        )
                                    )
                                }
                                localSelectedPots.add(item)
                            }
                            println("PotsSection $selectedPots")
                        })
                        .widthIn(60.dp, Dp.Infinity)
                        .height(90.dp)
                        .background(
                            color = if (localSelectedPots.contains(item)) {
                                MaterialTheme.colors.primary.copy(alpha = 0.5f)
                            } else {
                                println("PotsSection $selectedPots  ${selectedPots?.contains(item)} not selected")
                                MaterialTheme.colors.onSurface.copy(alpha = 0.0f)
                            }, potShape
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Row {
                            Column(
                                modifier = Modifier.width(60.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Box(
                                    contentAlignment = Alignment.BottomCenter,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    if (IconDictionary.allIcons[item.icon] != null) {
                                        Image(
                                            painter = painterResource(id = IconDictionary.allIcons[item.icon]!!),
                                            contentDescription = item.title,
                                            modifier = Modifier.size(40.dp),
                                            colorFilter = ColorFilter.tint(
                                                MaterialTheme.colors.primary.copy(
                                                    alpha = 0.5f
                                                )
                                            )

                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = item.title!!,
                            color = MaterialTheme.colors.onSurface,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp, 8.dp)
                        )

                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}