package com.cureius.pocket.feature_transaction.domain.util


sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
