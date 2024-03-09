package com.cureius.pocket.feature_upi_payment.presentation.manager

sealed class UpiPaymentEvent {
    data class SetPaymentAmount(val amount: Double) : UpiPaymentEvent()
    data class SetPaymentReceiverName(val receiverName: String) : UpiPaymentEvent()
    data class SetPaymentReceiverId(val receiverId: String) : UpiPaymentEvent()
    data class SetPaymentDescription(val description: String) : UpiPaymentEvent()
    data class SetPaymentSuccessful(val isSuccessful: Boolean) : UpiPaymentEvent()
}