package com.cureius.pocket.feature_upi_payment.presentation.manager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpiPaymentViewModel @Inject constructor(

) : ViewModel() {
    private val _state = mutableStateOf(UpiPaymentState())
    val state: State<UpiPaymentState> = _state

    fun onEvent(event: UpiPaymentEvent) {
        when (event) {
            is UpiPaymentEvent.SetPaymentAmount -> {
                _state.value = _state.value.copy(paymentAmount = event.amount)
            }
            is UpiPaymentEvent.SetPaymentReceiverName -> {
                _state.value = _state.value.copy(paymentReceiverName = event.receiverName)
            }
            is UpiPaymentEvent.SetPaymentReceiverId -> {
                _state.value = _state.value.copy(paymentReceiverId = event.receiverId)
            }
            is UpiPaymentEvent.SetPaymentDescription -> {
                _state.value = _state.value.copy(paymentDescription = event.description)
            }
            is UpiPaymentEvent.SetPaymentSuccessful -> {
                _state.value = _state.value.copy(isPaymentSuccessful = event.isSuccessful)
            }
        }
    }

}
