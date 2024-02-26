package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.add_transaction.UpdateTransactionEvent
import com.cureius.pocket.feature_transaction.presentation.add_transaction.UpdateTransactionViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 50.dp,
    cutCornerSize: Dp = 0.dp,
    showDate: Boolean = true,
    potsViewModel: PotsViewModel? = hiltViewModel(),
    updateTransactionViewModel: UpdateTransactionViewModel = hiltViewModel(),
    onDeleteClick: () -> Unit,
) {
    val potShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )
    val selectedPot = remember {
        mutableStateOf<Pot?>(
            null
        )
    }

    var transactionTitle by remember {
        mutableStateOf("Give title")
    }

    var transactionTitleFiledFocused by remember {
        mutableStateOf(false)
    }

    val pots = potsViewModel?.templatePots?.value ?: emptyList()
    Column(
        modifier = modifier.background(
            MaterialTheme.colors.background, RoundedCornerShape(30.dp)
        )
    ) {
        transaction.date?.let {
            if (showDate) {
                Text(
                    text = LocalDate.ofEpochDay(it)
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Box(
            modifier = modifier
                .background(
                    MaterialTheme.colors.surface.copy(0.1f), RoundedCornerShape(30.dp)
                )
                .border(
                    width = 1.dp, color = if (transaction.type.equals(
                            "debited", ignoreCase = true
                        ) && transaction.pot.isNullOrBlank() && pots.isNotEmpty()
                    ) MaterialTheme.colors.onSurface.copy(alpha = 0.2f) else {
                        Color.Transparent
                    }, shape = RoundedCornerShape(30.dp)
                )
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val clipPath = Path().apply {
                    lineTo(size.width - cutCornerSize.toPx(), 0f)
                    lineTo(size.width, cutCornerSize.toPx())
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                clipPath(clipPath) {
                    drawRoundRect(
                        color = Color.Transparent,
                        size = size,
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(12.dp, 4.dp, 12.dp, 4.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .background(
                                    color = if (transactionTitleFiledFocused) {
                                        MaterialTheme.colors.primary
                                    } else {
                                        if (transaction.type
                                                ?.lowercase()
                                                .equals("credited", true)
                                        ) {
                                            MaterialTheme.colors.secondary
                                        } else {
                                            MaterialTheme.colors.error
                                        }
                                    }, CircleShape
                                )
                                .padding(8.dp)
                                .clickable {
                                    if (transactionTitleFiledFocused && transactionTitle.isNotBlank()) {
                                        updateTransactionViewModel.onEvent(
                                            UpdateTransactionEvent.PickTransaction(
                                                transaction
                                            )
                                        )
                                        updateTransactionViewModel.onEvent(
                                            UpdateTransactionEvent.EnteredTitle(
                                                transactionTitle
                                            )
                                        )
                                        updateTransactionViewModel.onEvent(
                                            UpdateTransactionEvent.UpdateTransactionTitle
                                        )
                                        transactionTitleFiledFocused = false
                                    }
                                }
                        ) {
                            Image(
                                painter = if (transactionTitleFiledFocused) {
                                    painterResource(R.drawable.baseline_done_24)
                                } else {
                                    painterResource(id = IconDictionary.allIcons["outGoingArrow"]!!)
                                },
                                contentDescription = "outGoingArrow",
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(
                                        degrees = if (transaction.type
                                                ?.lowercase()
                                                .equals("credited", true) && !transactionTitleFiledFocused
                                        ) {
                                            180f
                                        } else {
                                            0f
                                        }
                                    ),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colors.surface
                                )

                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            if (transaction.title != null) Text(
                                text = transaction.title.toString(),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            ) else {
//                                TextField(
//                                    value = "Give title",
//                                    onValueChange = { amount ->
//                                        // Ensure that the input is numeric
////                                        if (amount.all { it.isDigit() }) {
////                                            viewModel.onEvent(
////                                                AddTransactionEvent.EnteredAmount(
////                                                    amount
////                                                )
////                                            )
////                                        }
//                                    },
//                                    textStyle = MaterialTheme.typography.h6.copy(
//                                        color = MaterialTheme.colors.onSurface
//                                    ),
//                                    label = { Text(text = "How much?") },
//                                    placeholder = { Text(text = "Enter Transaction Amount") },
//                                )
                                Row {
                                    BasicTextField(value = transactionTitle,
                                        onValueChange = {
                                            transactionTitle = it
                                            transactionTitleFiledFocused = it.isNotBlank()
                                        },
                                        maxLines = 3,
                                        textStyle = MaterialTheme.typography.h6.copy(
                                            color = if (transactionTitleFiledFocused) MaterialTheme.colors.onSurface.copy(
                                                1f
                                            ) else MaterialTheme.colors.onSurface.copy(0.4f)
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .onFocusChanged {
                                                if (transactionTitle.isEmpty()) {
                                                    transactionTitle =
                                                        "Give title";
                                                    transactionTitleFiledFocused = false
                                                }
                                            }
                                            .onFocusEvent {

                                            }
                                    )
                                }
                            }
                            Text(
                                text = if (transaction.type?.lowercase().equals("credited", true)) {
                                    "+" + transaction.amount.toString()
                                } else {
                                    "-" + transaction.amount.toString()
                                },
                                style = MaterialTheme.typography.body1,
                                color = if (transaction.type?.lowercase()
                                        .equals("credited", true)
                                ) {
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                                } else {
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
//                    transaction.body.let {
//                        if (it != null) {
//                            if (it.isNotEmpty()) {
//                                Text(
//                                    text = transaction.body.toString().split(" ")
//                                        .joinToString(" ") { it.capitalize() },
//                                    style = MaterialTheme.typography.body2,
//                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
//                                    maxLines = 2,
//                                    overflow = TextOverflow.Ellipsis,
//                                    modifier = Modifier.fillMaxWidth(0.75f)
//                                )
//                            }
//                        }
//                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        if (transaction.pot != null) transaction.pot.let {
                            Box(
                                contentAlignment = Alignment.BottomCenter,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colors.secondary.copy(0.05f),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .size(40.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    Row {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.BottomCenter,
                                                modifier = Modifier.padding(8.dp)
                                            ) {
                                                if (IconDictionary.allIcons[pots.find { pot ->
                                                        it.equals(
                                                            pot.title, true
                                                        )
                                                    }?.icon] != null) {
                                                    Image(
                                                        painter = painterResource(id = IconDictionary.allIcons[pots.find { pot ->
                                                            it.equals(
                                                                pot.title, true
                                                            )
                                                        }?.icon]!!),
                                                        contentDescription = "Pot",
                                                        colorFilter = ColorFilter.tint(
                                                            MaterialTheme.colors.primary.copy(
                                                                alpha = 0.7f
                                                            )
                                                        )

                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        transaction.event_timestamp?.let {
                            val instant = Instant.ofEpochMilli(it)
                            val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
                            val localTime = localDateTime.toLocalTime()
                            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                            val formattedTime = localTime.format(formatter)
                            Row {
                                Text(
                                    text = formattedTime, style = TextStyle(
                                        fontWeight = FontWeight.Light, fontSize = 14.sp
                                    ), color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                if (transaction.type.equals(
                        "debited", ignoreCase = true
                    ) && transaction.pot.isNullOrBlank() && pots.isNotEmpty()
                ) {
                    Column {
                        Text(
                            text = "Choose Pot",
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Start,
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .fillMaxWidth()

                        )// Choose Pots Header
                        LazyRow(
                            modifier = Modifier.height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(pots) { item ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            if (selectedPot.value == item) {
                                                selectedPot.value = null
                                                updateTransactionViewModel.onEvent(
                                                    UpdateTransactionEvent.SelectedPot(
                                                        null
                                                    )
                                                )
                                            } else {
                                                selectedPot.value = item
                                                updateTransactionViewModel.onEvent(
                                                    UpdateTransactionEvent.SelectedPot(
                                                        item
                                                    )
                                                )
                                            }
                                        })
                                        .widthIn(60.dp, Dp.Infinity)
                                        .height(90.dp)
                                        .background(
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                            potShape
                                        )
                                        .border(
                                            1.dp, color = if (selectedPot.value == item) {
                                                MaterialTheme.colors.primary.copy(alpha = 0.5f)
                                            } else {
                                                MaterialTheme.colors.onSurface.copy(alpha = 0.0f)
                                            }, potShape
                                        ),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(if (selectedPot.value == item) 120.dp else 60.dp)
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
                                            if (selectedPot.value == item) {
                                                Column(
                                                    modifier = Modifier.width(60.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Bottom
                                                ) {
                                                    Box(contentAlignment = Alignment.BottomCenter,
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .background(
                                                                color = MaterialTheme.colors.primary.copy(
                                                                    alpha = 0.5f
                                                                ), shape = CircleShape
                                                            )
                                                            .clickable {
                                                                updateTransactionViewModel.onEvent(
                                                                    UpdateTransactionEvent.PickTransaction(
                                                                        transaction
                                                                    )
                                                                )
                                                                updateTransactionViewModel.onEvent(
                                                                    UpdateTransactionEvent.SelectedPot(
                                                                        item
                                                                    )
                                                                )
                                                                updateTransactionViewModel.onEvent(
                                                                    UpdateTransactionEvent.UpdateTransaction
                                                                )
                                                            }) {
                                                        Image(
                                                            painter = painterResource(R.drawable.add),
                                                            contentDescription = item.title,
                                                            modifier = Modifier.size(40.dp),
                                                            colorFilter = ColorFilter.tint(
                                                                MaterialTheme.colors.onBackground
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
                        }// Row Of Selectable Pre-added Pots
                        Spacer(modifier = Modifier.height(10.dp)) // Spacer 20
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun IncomeTransactionItemPreview() {
    TransactionItem(
        transaction = Transaction(
            title = "Salary",
            type = "credited",
            amount = 100.0,
            body = "This is a transaction",
            color = Color.White.toArgb(),
            timestamp = System.currentTimeMillis()
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {}
}


@Preview
@Composable
fun SpendingTransactionItemPreview() {
    TransactionItem(
        transaction = Transaction(
            title = "Food",
            type = "debited",
            amount = 100.0,
            body = null,
            color = Color.White.toArgb(),
            timestamp = System.currentTimeMillis(),
            date = LocalDate.now().toEpochDay()
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {}
}