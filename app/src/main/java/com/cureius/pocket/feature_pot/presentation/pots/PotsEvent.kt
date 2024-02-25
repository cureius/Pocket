package com.cureius.pocket.feature_pot.presentation.pots

sealed class PotsEvent {
    data class MonthSelected(val value: String) : PotsEvent()
    object ToggleMonthPickerDialog : PotsEvent()

}