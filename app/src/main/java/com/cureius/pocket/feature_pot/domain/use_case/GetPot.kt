package com.cureius.pocket.feature_pot.domain.use_case

import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.repository.PotRepository

class GetPot(
    private val repository: PotRepository
) {
    suspend operator fun invoke(id: Int): Pot? {
        return repository.getPotById(id)
    }
}