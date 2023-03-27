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

    private val _startSyncPromptVisibility = mutableStateOf(false)
    val startSyncPromptVisibility: State<Boolean> = _startSyncPromptVisibility

    private val _creditDetectionPromptVisibility = mutableStateOf(false)
    val creditDetectionPromptVisibility: State<Boolean> = _creditDetectionPromptVisibility

    private val _totalBalance = mutableStateOf("1,50,000")
    val totalBalance: State<String> = _totalBalance


}