package com.cureius.pocket.feature_pot.presentation.configure_pots

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
class ConfigurePotsViewModel @Inject constructor(
    private val potUseCases: PotUseCases
) : ViewModel() {


    private val _state = mutableStateOf(listOf<Pot>())
    val state: State<List<Pot>> = _state
    private var getPotsJob: Job? = null

    private val _nodes = mutableStateOf(mutableListOf<MutableState<Float>?>())
    val nodes : State<MutableList<MutableState<Float>?>> = _nodes

    init {
        getTemplatePots()
    }

    private fun getTemplatePots() {
        getPotsJob?.cancel()
        getPotsJob = potUseCases.getPots().onEach { pots ->
            _state.value = pots.filter { it.is_template == true }
            _nodes.value =
                _state.value.map { it.weight?.let { weight -> mutableStateOf(weight) } }.toMutableList()
        }.launchIn(viewModelScope)
    }


}