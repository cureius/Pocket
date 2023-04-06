package com.cureius.pocket.feature_dashboard.presentation.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(

) : ViewModel(){

    private val _infoSectionVisibility = mutableStateOf(false)
    val infoSectionVisibility: State<Boolean> = _infoSectionVisibility

    private val _addAccountPromptVisibility = mutableStateOf(false)
    val addAccountPromptVisibility: State<Boolean> = _addAccountPromptVisibility

    private val _addPotPromptVisibility = mutableStateOf(false)
    val addPotPromptVisibility: State<Boolean> = _addPotPromptVisibility

    private val _startSyncPromptVisibility = mutableStateOf(true)
    val startSyncPromptVisibility: State<Boolean> = _startSyncPromptVisibility

    private val _permissionPrompt = mutableStateOf(false)
    val permissionPrompt: State<Boolean> = _permissionPrompt

    private val _creditDetectionPromptVisibility = mutableStateOf(false)
    val creditDetectionPromptVisibility: State<Boolean> = _creditDetectionPromptVisibility

    private val _totalBalance = mutableStateOf("------")
    val totalBalance: State<String> = _totalBalance

    private val _incomeMtd = mutableStateOf("------")
    val incomeMtd: State<String> = _incomeMtd

    private val _spentMtd = mutableStateOf("------")
    val spentMtd: State<String> = _spentMtd

    fun onEvent(event: DashBoardEvent){
        when (event){
            is DashBoardEvent.ToggleInfoSectionVisibility -> {
                _infoSectionVisibility.value = !infoSectionVisibility.value
            }
            is DashBoardEvent.ToggleAskPermission -> {
                _permissionPrompt.value = !permissionPrompt.value
            }
            is DashBoardEvent.ChangeTotalBalance -> {
                _totalBalance.value = event.balance
            }
            is DashBoardEvent.ChangeIncomeMtd -> {
                _incomeMtd.value = event.value
            }
            is DashBoardEvent.ChangeSpentMtd -> {
                _spentMtd.value = event.value
            }
        }
    }


}