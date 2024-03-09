package com.cureius.pocket.feature_pot.presentation.update_pot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdatePotViewModel @Inject constructor(
    private val potUseCases: PotUseCases
) : ViewModel() {

    private val _dialogVisibility = mutableStateOf(false)
    val dialogVisibility: State<Boolean> = _dialogVisibility

    private val _selectedPot = mutableStateOf<Pot?>(null)
    val selectedPot: State<Pot?> = _selectedPot

    private val _potTitle = mutableStateOf("")
    val potTitle: State<String> = _potTitle

    private val _potIcon = mutableStateOf("")
    val potIcon: State<String> = _potIcon

    private val _potType = mutableStateOf("")
    val potType: State<String> = _potType

    private val _isTemporary = mutableStateOf(false)
    val isTemporary: State<Boolean> = _isTemporary

    private val _isDefault = mutableStateOf(false)
    val isDefault: State<Boolean> = _isDefault

    private val _potValidity = mutableStateOf(null as Long?)
    val potValidity: State<Long?> = _potValidity


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPotId: Int? = null

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SavePot : UiEvent()
    }

    fun onEvent(event: UpdatePotEvent) {
        when (event) {
            is UpdatePotEvent.ToggleUpdateAccountDialog -> {
                _dialogVisibility.value = !_dialogVisibility.value
            }

            is UpdatePotEvent.SelectPot -> {
                _selectedPot.value = event.value
            }

            is UpdatePotEvent.EnteredTitle -> {
                _potTitle.value = event.value
            }

            is UpdatePotEvent.EnteredValidity -> {
                _potValidity.value = event.value
            }

            is UpdatePotEvent.SelectedIcon -> {
                _potIcon.value = event.value
            }

            is UpdatePotEvent.SelectedType -> {
                _potType.value = event.value
            }

            is UpdatePotEvent.ToggledIsTemporary -> {
                _isTemporary.value = event.value
            }

            is UpdatePotEvent.ToggledIsDefault -> {
                _isDefault.value = event.value
            }

            is UpdatePotEvent.SavePot -> {
                viewModelScope.launch {
                    try {
                        potUseCases.updatePot(
                            _selectedPot.value?.copy(
                                title = _potTitle.value,
                                icon = _potIcon.value,
                            ) ?: return@launch
                        )
                        _potTitle.value = ""
                        _potType.value = ""
                        _potIcon.value = ""
                        _eventFlow.emit(UiEvent.SavePot)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Save Pot"
                            )
                        )
                    }
                }
            }

            is UpdatePotEvent.DeletePot -> {
                viewModelScope.launch {
                    try {
                        potUseCases.deletePot(
                            _selectedPot.value ?: return@launch
                        )
                        _potTitle.value = ""
                        _potType.value = ""
                        _potIcon.value = ""
                        _eventFlow.emit(UiEvent.SavePot)
                    } catch (e: InvalidTransactionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Cant Delete Pot"
                            )
                        )
                    }
                }
            }
        }
    }
}