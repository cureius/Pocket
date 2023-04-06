package com.cureius.pocket.feature_dashboard.presentation.dashboard

import com.cureius.pocket.feature_account.presentation.add_account.AddAccountEvent

sealed class DashBoardEvent {
    object ToggleInfoSectionVisibility : DashBoardEvent()
    object ToggleAskPermission : DashBoardEvent()
    data class ChangeTotalBalance(val balance : String) : DashBoardEvent()
    data class ChangeIncomeMtd(val value : String) : DashBoardEvent()
    data class ChangeSpentMtd(val value : String) : DashBoardEvent()
}