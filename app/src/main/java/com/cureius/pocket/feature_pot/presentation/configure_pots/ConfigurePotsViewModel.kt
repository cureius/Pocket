package com.cureius.pocket.feature_pot.presentation.configure_pots

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    val updatedPots: MutableList<Pot> = _updatedPots

    private var getPotsJob: Job? = null

    private val _weights = mutableStateOf(mutableListOf<MutableState<Float>?>())
    val weights: State<MutableList<MutableState<Float>?>> = _weights

    private val _nodes = mutableStateOf(mutableListOf<MutableState<Float>?>())
    val nodes: State<MutableList<MutableState<Float>?>> = _nodes


    init {
        getTemplatePots()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onEvent(event: ConfigurePotsEvent) {
        when (event) {
            is ConfigurePotsEvent.EnteredIncome -> {
                _income.value = event.value
            }

            is ConfigurePotsEvent.RangeChange -> {
                _nodes.value[event.nodeIndex]?.value = event.value
            }

            is ConfigurePotsEvent.SaveConfiguration -> {
                viewModelScope.launch {
                    try {
                        potUseCases.addPots(updatedPots)
                    } catch (e: InvalidTransactionException) {

                    }
                }
            }

            is ConfigurePotsEvent.UpdatePot -> {
                val firstElement =
                    state.value.first { it.id == event.id }.copy(weight = event.value)
                _updatedPots.removeIf { it.id == event.id }
                _updatedPots.add(firstElement)
                Log.d("Changing List", "onEvent: ${updatedPots}")
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
            _nodes.value.add(
                mutableStateOf(
                    0.0f
                )
            )
            weights.value.forEachIndexed { index, mutableState ->
                Log.d("weight", "getTemplatePots: $index-->$mutableState")
                if (mutableState != null) {
                    _nodes.value.add(
                        index + 1, if (index == _weights.value.size - 1) {
                            if (mutableState.value + nodes.value[index]?.value!! != 1.0f) {
                                mutableStateOf(
                                    1.0f
                                )
                            } else {
                                Log.d("previous node", "getTemplatePots: ${nodes.value[index - 1]?.value}")
                                mutableStateOf(
                                    mutableState.value +
                                        _nodes.value[index]?.value!!
                                )
                            }
                        } else {
                            mutableStateOf(
                                mutableState.value + (if (index > 0) {
                                    Log.d("previous node", "getTemplatePots: ${nodes.value[index - 1]?.value}")
                                    _nodes.value[index]?.value
                                } else {
                                    0.0f
                                })!!
                            )
                        }
                    )
                }else{
                    _nodes.value.add(
                        index + 1, if (index == _weights.value.size - 1) {
                            if (0.0f + nodes.value[index]?.value!! != 1.0f) {
                                mutableStateOf(
                                    1.0f
                                )
                            } else {
                                Log.d("previous node", "getTemplatePots: ${nodes.value[index - 1]?.value}")
                                mutableStateOf(
                                    0.0f +
                                            _nodes.value[index]?.value!!
                                )
                            }
                        } else {
                            mutableStateOf(
                                0.0f + (if (index > 0) {
                                    Log.d("previous node", "getTemplatePots: ${nodes.value[index - 1]?.value}")
                                    _nodes.value[index]?.value
                                } else {
                                    0.0f
                                })!!
                            )
                        }
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}