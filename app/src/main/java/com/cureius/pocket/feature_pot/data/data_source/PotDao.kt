package com.cureius.pocket.feature_pot.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.cureius.pocket.feature_pot.domain.model.Pot

@Dao
interface PotDao {
    @Query("SELECT * FROM `pot`")
    fun getPots(): Flow<List<Pot>>

    @Query("SELECT * FROM `pot` WHERE id = :id")
    suspend fun getPotById(id: Int): Pot?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPot(pot: Pot)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPots(pots: List<Pot>)

    @Delete
    suspend fun deletePot(pot: Pot)

}