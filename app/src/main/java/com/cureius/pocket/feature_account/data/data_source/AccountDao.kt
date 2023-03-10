package com.cureius.pocket.feature_account.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cureius.pocket.feature_account.domain.model.Account
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountDao {

    @Query("SELECT * FROM `account`")
    fun getAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM `account` WHERE id = :id")
    suspend fun getAccountById(id: Int): Account?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)
}