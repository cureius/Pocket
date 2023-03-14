package com.cureius.pocket.feature_dashboard.domain

sealed class PotType(val type: String){
    object Asset: PotType(type = "asset")
    object Liability: PotType(type = "liability")
}
