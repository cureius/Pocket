package com.cureius.pocket.feature_pot.presentation.configure_pots

sealed class ConfigurePotsEvent {
    data class EnteredIncome(val value: String) : ConfigurePotsEvent()

    data class RangeChange(val nodeIndex: Int, val value: Float) : ConfigurePotsEvent()
    data class UpdatePot(val potIndex: Int, val value: Float) : ConfigurePotsEvent()
    object SaveConfiguration : ConfigurePotsEvent()
}