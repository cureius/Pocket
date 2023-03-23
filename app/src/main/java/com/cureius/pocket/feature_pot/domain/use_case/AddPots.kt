package com.cureius.pocket.feature_pot.domain.use_case

import com.cureius.pocket.feature_pot.domain.model.InvalidPotException
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.repository.PotRepository

class AddPots(
    private val repository: PotRepository
) {
    @Throws(InvalidPotException::class)
    suspend operator fun invoke(pots: List<Pot>) {
        repository.insertPots(pots)
    }
}