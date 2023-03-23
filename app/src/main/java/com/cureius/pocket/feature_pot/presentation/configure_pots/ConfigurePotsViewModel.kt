package com.cureius.pocket.feature_pot.presentation.configure_pots

import android.util.Log
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

    private val _income = mutableStateOf("")
    val income: State<String> = _income


    private val _state = mutableStateOf(listOf<Pot>())
    val state: State<List<Pot>> = _state
    private var _updatedPots = mutableListOf<Pot>()

    private var getPotsJob: Job? = null

    private val _weights = mutableStateOf(mutableListOf<MutableState<Float>?>())
    val weights: State<MutableList<MutableState<Float>?>> = _weights

    private val _nodes = mutableStateOf(mutableListOf<MutableState<Float>?>())
    val nodes: State<MutableList<MutableState<Float>?>> = _nodes


    init {
        getTemplatePots()
        _updatedPots = state.value.toMutableList()
        Log.d("updating list", "onEvent: ${_updatedPots.size}")

    }

    fun onEvent(event: ConfigurePotsEvent) {
        when (event) {
            is ConfigurePotsEvent.EnteredIncome -> {
                _income.value = event.value
            }

            is ConfigurePotsEvent.RangeChange -> {
                _nodes.value[event.nodeIndex]?.value = event.value
            }

            is ConfigurePotsEvent.UpdatePot -> {
                Log.d("updating list", "onEvent: ${event.toString()}")
                var index: Int
                if (_updatedPots.any {
                        it.id == _state.value[event.potIndex].id
                    }) {

                    index = _updatedPots.indexOf(_state.value[event.potIndex])
                    if (index > 0) {
                        _updatedPots.add(
                            index, _state.value[event.potIndex].copy(weight = event.value)
                        )
                    }

                } else {
                    _updatedPots.add(
                        _state.value[event.potIndex].copy(weight = event.value)
                    )
                }

                Log.d("updated list", "onEvent: $_updatedPots")
            }
        }
    }

    private fun getTemplatePots() {
        getPotsJob?.cancel()
        getPotsJob = potUseCases.getPots().onEach { pots ->
            _state.value = pots.filter { it.is_template == true }
            _weights.value =
                _state.value.map { it.weight?.let { weight -> mutableStateOf(weight) } }
                    .toMutableList()
            Log.d("Weight", "getTemplatePots: ${_weights.value.toString()}")
            _nodes.value.add(
                mutableStateOf(
                    0.0f
                )
            )
            _weights.value.forEachIndexed { index, mutableState ->
                if (mutableState != null) {
                    _nodes.value.add(
                        index + 1, mutableStateOf(
                            mutableState.value + (if (index > 0) {
                                _nodes.value[index - 1]?.value
                            } else {
                                0.0f
                            })!!
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


}