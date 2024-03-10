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

    private val _amount = mutableStateOf<Double?>(null)
    val amount: State<Double?> = _amount

    private val _receiverName = mutableStateOf<String?>(null)
    val receiverName: State<String?> = _receiverName

    private val _receiverId = mutableStateOf<String?>(null)
    val receiverId: State<String?> = _receiverId

    private val _description = mutableStateOf<String?>(null)
    val description: State<String?> = _description

    fun onEvent(event: UpiPaymentEvent) {
        when (event) {
            is UpiPaymentEvent.SetPaymentAmount -> {
                _amount.value = event.amount
            }
            is UpiPaymentEvent.SetPaymentReceiverName -> {
                _receiverName.value = event.receiverName
            }
            is UpiPaymentEvent.SetPaymentReceiverId -> {
                _receiverId.value = event.receiverId
            }
            is UpiPaymentEvent.SetPaymentDescription -> {
                _description.value = event.description
            }
            is UpiPaymentEvent.SetPaymentSuccessful -> {
                _state.value = _state.value.copy(isPaymentSuccessful = event.isSuccessful)
            }
        }
    }

}
