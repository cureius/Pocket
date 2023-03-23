package com.cureius.pocket.feature_pot.domain.use_case

data class PotUseCases(
    val getPot: GetPot,
    val getPots: GetPots,
    val addPot: AddPot,
    val addPots: AddPots,
    val deletePot: DeletePot
)