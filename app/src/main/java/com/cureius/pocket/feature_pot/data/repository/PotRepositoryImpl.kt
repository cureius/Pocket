package com.cureius.pocket.feature_pot.data.repository

import com.cureius.pocket.feature_pot.data.data_source.PotDao
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.repository.PotRepository
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotEvent
import kotlinx.coroutines.flow.Flow

class PotRepositoryImpl(
    private val dao: PotDao
) : PotRepository {
    override fun getPots(): Flow<List<Pot>> {
        return dao.getPots()
    }

    override fun getTemplatePots(): Flow<List<Pot>> {
        return dao.getTemplatePots()
    }

    override suspend fun getPotById(id: Int): Pot? {
        return dao.getPotById(id)
    }

    override suspend fun insertPot(pot: Pot) {
        return dao.insertPot(pot)
    }

    override suspend fun updatePot(pot: Pot) {
        return dao.updatePot(pot)
    }

    override suspend fun insertPots(pots: List<Pot>) {
        return dao.insertPots(pots)
    }

    override suspend fun deletePot(pot: Pot) {
        return dao.deletePot(pot)
    }
    override fun getPotsWithValidity(validity: Long) : Flow<List<Pot>>  {
        return dao.getPotsWithValidity(validity)
    }

}