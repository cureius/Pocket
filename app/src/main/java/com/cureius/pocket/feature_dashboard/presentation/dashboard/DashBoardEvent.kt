package com.cureius.pocket.feature_dashboard.presentation.dashboard

sealed class DashBoardEvent {
    object ToggleInfoSectionVisibility : DashBoardEvent()
    object ToggleAskPermission : DashBoardEvent()
    object TurnOnBalanceCalibration : DashBoardEvent()
    object TurnOffBalanceCalibration : DashBoardEvent()
    object SaveAllTransactions : DashBoardEvent()
    object ReadAllSMS : DashBoardEvent()
    data class ChangeTotalBalance(val balance : String) : DashBoardEvent()
    data class ChangeIncomeMtd(val value : String) : DashBoardEvent()
    data class ChangeSpentMtd(val value : String) : DashBoardEvent()
}