package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending),
    onOrderChange: (TransactionOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(text = "Title",
                selected = transactionOrder is TransactionOrder.Title,
                onSelect = { onOrderChange(TransactionOrder.Title(transactionOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Date",
                selected = transactionOrder is TransactionOrder.Date,
                onSelect = { onOrderChange(TransactionOrder.Date(transactionOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Color",
                selected = transactionOrder is TransactionOrder.Color,
                onSelect = { onOrderChange(TransactionOrder.Color(transactionOrder.orderType)) })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(text = "Ascending",
                selected = transactionOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(transactionOrder.copy(OrderType.Ascending)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Descending",
                selected = transactionOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(transactionOrder.copy(OrderType.Descending)) })
        }

    }

}