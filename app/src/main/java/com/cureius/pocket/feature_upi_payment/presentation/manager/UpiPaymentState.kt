package com.cureius.pocket.feature_upi_payment.presentation.manager

data class UpiPaymentState(
    val paymentAmount: Double? = null,
    val paymentReceiverName: String? = null,
    val paymentReceiverId: String? = null,
    val paymentDescription: String? = null,
    val isPaymentSuccessful: Boolean = false
)