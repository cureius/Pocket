package com.cureius.pocket.feature_pot.presentation.add_pot

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class AddPotViewModel @Inject constructor(
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

    private val _potValidity = mutableStateOf(System.currentTimeMillis())
    val potValidity: State<Long> = _potValidity


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPotId: Int? = null

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SavePot : UiEvent()
    }

    fun onEvent(event: AddPotEvent) {
        when (event) {
            is AddPotEvent.EnteredTitle -> {
                _potTitle.value = event.value
            }

            is AddPotEvent.EnteredValidity -> {
                _potValidity.value = event.value
            }

            is AddPotEvent.SelectedIcon -> {
                _potIcon.value = event.value
            }

            is AddPotEvent.SelectedType -> {
                _potType.value = event.value
            }

            is AddPotEvent.ToggledIsTemporary -> {
                _isTemporary.value = event.value
            }

            is AddPotEvent.ToggledIsDefault -> {
                _isDefault.value = event.value
            }

            is AddPotEvent.SavePot -> {
                viewModelScope.launch {
                    try {
                        potUseCases.addPot(
                            Pot(
                                type = potType.value,
                                validity = potValidity.value,
                                icon = potIcon.value,
                                title = potTitle.value,
                                is_temporary = isTemporary.value,
                                is_template = true,
                                is_monthly = false,
                                is_default = isDefault.value,
                                timestamp = System.currentTimeMillis(),
                                id = currentPotId,
                                weight = 0.0f
                            )
                        )
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
        }
    }
}