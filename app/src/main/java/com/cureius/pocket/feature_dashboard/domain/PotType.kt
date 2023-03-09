package com.cureius.pocket.feature_dashboard.domain

sealed class PotType{
    object Asset: PotType()
    object Liability: PotType()
}
