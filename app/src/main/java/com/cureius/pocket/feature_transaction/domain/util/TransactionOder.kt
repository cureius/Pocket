package com.cureius.pocket.feature_transaction.domain.util

sealed class TransactionOrder(val orderType: OrderType) {
    class Date(orderType: OrderType) : TransactionOrder(orderType)
    class Type(orderType: OrderType) : TransactionOrder(orderType)
    class Amount(orderType: OrderType) : TransactionOrder(orderType)

    fun copy(orderType: OrderType): TransactionOrder {
        return when (this) {
            is Amount -> Amount(orderType)
            is Date -> Date(orderType)
            is Type -> Type(orderType)
        }
    }
}