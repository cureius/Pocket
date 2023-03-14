package com.cureius.pocket.feature_pot.data.repository

import com.cureius.pocket.feature_pot.data.data_source.PotDao
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.repository.PotRepository
import kotlinx.coroutines.flow.Flow

class PotRepositoryImpl(
    private val dao: PotDao
) : PotRepository {
    override fun getPots(): Flow<List<Pot>> {
        return dao.getPots()
    }

    override suspend fun getPotById(id: Int): Pot? {
        return dao.getPotById(id)
    }

    override suspend fun insertPot(pot: Pot) {
        return dao.insertPot(pot)
    }

    override suspend fun deletePot(pot: Pot) {
        return dao.deletePot(pot)
    }

}