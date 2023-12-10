package com.cureius.pocket.feature_pot.domain.use_case

import com.cureius.pocket.feature_pot.domain.model.InvalidPotException
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.repository.PotRepository

class AddPot(
    private val repository: PotRepository
) {
    @Throws(InvalidPotException::class)
    suspend operator fun invoke(pot: Pot) {
        if (pot.title?.isBlank() == true) {
            throw InvalidPotException("A Pot Need A Title")
        }
        repository.insertPot(pot)
    }
}