package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
            DefaultRadioButton(text = "Date",
                selected = transactionOrder is TransactionOrder.Date,
                onSelect = { onOrderChange(TransactionOrder.Date(transactionOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(text = "Type",
                selected = transactionOrder is TransactionOrder.Type,
                onSelect = { onOrderChange(TransactionOrder.Type(transactionOrder.orderType)) })
            Spacer(modifier = Modifier.width(8.dp))

            DefaultRadioButton(text = "Amount",
                selected = transactionOrder is TransactionOrder.Amount,
                onSelect = { onOrderChange(TransactionOrder.Amount(transactionOrder.orderType)) })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
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

@Composable
@Preview
fun OrderSectionPreview() {
    OrderSection(transactionOrder = TransactionOrder.Date(OrderType.Descending), onOrderChange = {})
}