package com.cureius.pocket.feature_pot.presentation.pots

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PotsViewModel @Inject constructor(
    private val potUseCases: PotUseCases
) : ViewModel() {

    private val _state = mutableStateOf(listOf<Pot>())
    val state: State<List<Pot>> = _state

    private val _pots = mutableStateOf(mutableListOf<Pot>())
    val pots: State<List<Pot>> = _pots

    private val _validPots = mutableStateOf(mutableListOf<Pot>())
    val validPots: State<List<Pot>> = _pots

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
    private suspend fun getPotsWithValidity(validity: Long) {
        getPotsJob?.cancel()
        getPotsJob = potUseCases.getPotsWithValidity(validity).onEach { pots ->
            _validPots.value = pots.toMutableList()
        }.launchIn(viewModelScope)
    }
}