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
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PotsViewModel @Inject constructor(
    private val potUseCases: PotUseCases
) : ViewModel() {

    private val _monthPickerDialogVisibility = mutableStateOf(false)
    val monthPickerDialogVisibility: State<Boolean> = _monthPickerDialogVisibility


    private val _state = mutableStateOf(listOf<Pot>())
    val state: State<List<Pot>> = _state

    private val _pots = mutableStateOf(mutableListOf<Pot>())
    val pots: State<List<Pot>> = _pots

    private val _validPots = mutableStateOf(listOf<Pot>())
    val validPots: State<List<Pot>> = _validPots

    private val _templatePots = mutableStateOf(listOf<Pot>())
    val templatePots: State<List<Pot>> = _templatePots

    private var getPotsJob: Job? = null
    private var getTemplatePotsJob: Job? = null
    private var getPotsWithValidityJob: Job? = null

    init {
        getPots()
        getTemplatePots()
        getPotsValidTillRunningMonth()
    }

    fun onEvent(event: PotsEvent) {
        when (event) {
            is PotsEvent.ToggleMonthPickerDialog -> {
                _monthPickerDialogVisibility.value = !_monthPickerDialogVisibility.value
            }
            is PotsEvent.MonthSelected -> {
                println("PotsViewModel: onEvent: MonthSelected: event: $event")
                val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                val date = LocalDate.parse("1/${event.value}", formatter)
                val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())
                val midnightLastDayOfMonth = lastDayOfMonth.atStartOfDay()
                val validityTimestamp = midnightLastDayOfMonth.toEpochSecond(ZoneOffset.UTC)
                getPotsWithValidity(validityTimestamp)
                _monthPickerDialogVisibility.value = false
            }
        }
    }

    private fun getPots() {
        getPotsJob?.cancel()
        getPotsJob = potUseCases.getPots().onEach { pots ->
            println("PotsViewModel: getPots: pots: $pots")
            _state.value = pots
        }.launchIn(viewModelScope)
    }

    private fun getTemplatePots() {
        getTemplatePotsJob?.cancel()
        getTemplatePotsJob = potUseCases.getTemplatePots().onEach { pots ->
            println("PotsViewModel: getTemplatePots: pots: $pots")
            _templatePots.value = pots
        }.launchIn(viewModelScope)
    }

    private fun getPotsWithValidity(validity: Long) {
        getPotsWithValidityJob?.cancel()
        getPotsWithValidityJob = potUseCases.getPotsWithValidity(validity).onEach { pots ->
            println("PotsViewModel: getPotsWithValidity: pots: $pots")
            _validPots.value = pots
        }.launchIn(viewModelScope)
    }

    private fun getPotsValidTillRunningMonth() {
        val currentDate = LocalDate.now()
        val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
        val midnightLastDayOfMonth = lastDayOfMonth.atStartOfDay()
        val validityTimestamp = midnightLastDayOfMonth.toEpochSecond(ZoneOffset.UTC)
        getPotsWithValidity(validityTimestamp)
    }
}