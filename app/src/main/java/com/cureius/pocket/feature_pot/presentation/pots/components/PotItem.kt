package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun PotItem(
    pot: Pot,
    isMonthFormat: Boolean = false,
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    data: MutableMap<String, Float>? = mutableMapOf(
        Pair("MONDAY", 0.0f),
        Pair("TUESDAY", 0.0f),
        Pair("WEDNESDAY", 0.0f),
        Pair("THURSDAY", 0.0f),
        Pair("FRIDAY", 0.0f),
        Pair("SATURDAY", 0.0f),
        Pair("SUNDAY", 0.0f),
    ),
    onPotEditClick: (Pot) -> Unit
) {

    val icon = IconDictionary.allIcons[pot.icon]
    val paddingModifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(168.dp)
        .background(MaterialTheme.colors.surface.copy(0.4f), RoundedCornerShape(16.dp))
    val transactions = transactionsViewModel.state.value.transactionsOnCurrentMonthForAccounts.filter { it.pot == pot.title }
    val totalAmount = transactions.sumOf { it.amount!! }
    val totalCapacity =
        transactionsViewModel.state.value.transactionsOnCurrentMonthForAccounts.filter { it.type == "credited" }
            .sumOf { it.amount!! }
    var actualCapacity = 0.0;
    var filled = 0.0f;
    if (pot.weight != null) {
        actualCapacity = totalCapacity * pot.weight!!
        if (actualCapacity.toFloat() != 0.0f) {
            filled = totalAmount.toFloat() / actualCapacity.toFloat();
        }
    }
    val edit = painterResource(R.drawable.outline_mode_edit_24)

    transactions.forEach {
        if (it.amount != null) {
            // Or, you can directly access the key and update its value
            data?.get(it.day)?.let { currentValue ->
                it.day?.let { it1 ->
                    data.put(
                        it1, currentValue + (it.amount.toFloat())
                    )
                }
            }
        }
    }
    var mData: Map<String, Float>? = data
    val maxValue = data?.values?.maxOrNull() ?: actualCapacity.toFloat()
    val updatedData = mData?.mapValues { (_, value) -> value / maxValue }

    val expenditureByDayOfMonth = transactions.getExpenditureByDayOfMonth()

    Box(
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
                                    MaterialTheme.colors.secondary.copy(0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                        ) {
                            val iconVector = icon?.let { ImageVector.vectorResource(id = it) }
                            if (iconVector != null) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = "add account",
                                    tint = MaterialTheme.colors.onBackground,
                                )
                            }
                        }
                        pot.title?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                onPotEditClick(pot)
                            },
                            modifier = Modifier.size(20.dp),
                            content = {
                                Image(
                                    painter = edit,
                                    contentDescription = "edit",
                                    modifier = Modifier.size(12.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface.copy(0.3f)),
                                    alignment = Alignment.Center
                                )
                            }
                        )

                    }
                    Box(
                        modifier = Modifier.background(
                            MaterialTheme.colors.primaryVariant.copy(0.1f), RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row {
                            if (totalAmount != null && actualCapacity != null) {
                                Text(
                                    text = "${totalAmount}/" + String.format(
                                        "%.1f",
                                        (actualCapacity)
                                    ),
                                    color = MaterialTheme.colors.onBackground,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontWeight = FontWeight.Normal),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            if (filled != null) {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp, 4.dp)
                                        .background(
                                            MaterialTheme.colors.secondary.copy(0.1f),
                                            RoundedCornerShape(4.dp)
                                        )
                                ) {
                                    Text(
                                        text = " "+String.format("%.2f", (filled!! * 100)) + "% ",
                                        color = MaterialTheme.colors.onBackground,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(fontWeight = FontWeight.Bold),
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }

                    }
                }
                Box(contentAlignment = Alignment.Center) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (filled != null) {
                                Box(
                                    modifier = Modifier
                                        .padding(0.dp, 20.dp, 0.dp, 0.dp)
                                ){
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight(filled * 1f)
                                            .fillMaxWidth(0.6f)
                                            .background(
                                                MaterialTheme.colors.primary.copy(alpha = 0.9f),
                                                RoundedCornerShape(8.dp)
                                            )
                                    )
                                }
                            }
                            Jar(MaterialTheme.colors.onSurface)
                        }
                        Box(
                            modifier = Modifier.background(Color.Yellow.copy(alpha = 0.0f))
                        ) {
                            if (isMonthFormat) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    QuadChart(
                                        data = expenditureByDayOfMonth,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .background(Color.Transparent)
                                            .align(CenterHorizontally)
                                    )
                                }
                            } else {
                                if (updatedData != null && maxValue != 0.0f) {
                                    Chart(
                                        data = updatedData!!,
                                        maxValue = maxValue.toDouble(),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .offset(y = (8).dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (pot.weight == null) {
                        MaterialTheme.colors.background.copy(alpha = 0.90f)
                    } else if (transactions.isEmpty()) {
                        MaterialTheme.colors.background.copy(alpha = 0.80f)
                    } else {
                        MaterialTheme.colors.background.copy(alpha = 0.0f)
                    }, shape = RoundedCornerShape(16.dp)
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (pot.weight == null) {
                Text(
                    text = "Set Weightage",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp, 0.dp)
                )
            } else if (transactions.isEmpty()) {
                Text(
                    text = "Not Date",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {

            }
        }

    }
}


fun List<Transaction>.getExpenditureByDayOfMonth(): List<Pair<Int, Double>> {
    // Filter transactions for the current month
    val currentMonthTransactions = this.filter {
        println("event_timestamp: ${it.event_timestamp}")
        val transactionDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(it.event_timestamp ?: 0), ZoneOffset.UTC
        )
//        transactionDate.monthValue == LocalDate.now().monthValue &&
        transactionDate.year == LocalDate.now().year
    }
    println("currentMonthTransactions: $currentMonthTransactions")
    // Group transactions by the day of the month and sum up the amounts for each day
    val expenditureMap = currentMonthTransactions.groupBy {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(it.event_timestamp ?: 0), ZoneOffset.UTC
        ).dayOfMonth
    }.mapValues { entry ->
        entry.value.sumOf { it.amount ?: 0.0 }
    }

    // Create a list of pairs with the base date of the month and the total amount expended on that day
    val expenditureByDayOfMonth = mutableListOf<Pair<Int, Double>>()
    val currentYearMonth = LocalDate.now().withDayOfMonth(1)
    val lastDayOfMonth = currentYearMonth.plusMonths(1).minusDays(1)
    for (dayOfMonth in 1..lastDayOfMonth.dayOfMonth) {
        val date = dayOfMonth
        val expenditure = expenditureMap[dayOfMonth] ?: 0.0
        expenditureByDayOfMonth.add(Pair(date, expenditure))
    }

    return expenditureByDayOfMonth
}
