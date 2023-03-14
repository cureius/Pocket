package com.cureius.pocket.feature_pot.presentation.pots

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_transaction.domain.util.OrderType
import com.cureius.pocket.feature_transaction.domain.util.TransactionOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class PotsViewModel @Inject constructor(
    private val potUseCases: PotUseCases
) : ViewModel() {

    var isDialogShown by mutableStateOf(false)
        private set

    fun onAddClick() {
        isDialogShown = true
    }

    fun onDismissDialog() {
        isDialogShown = false
    }

    private val _state = mutableStateOf(listOf<Pot>())
    val state: State<List<Pot>> = _state
    private var getPotsJob: Job? = null


    init {
        getPots()
    }

    private fun getPots() {
        getPotsJob?.cancel()
        getPotsJob = potUseCases.getPots().onEach { pots ->
            _state.value = pots
        }.launchIn(viewModelScope)
    }

}