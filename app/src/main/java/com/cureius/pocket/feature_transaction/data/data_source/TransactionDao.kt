package com.cureius.pocket.feature_transaction.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.cureius.pocket.feature_transaction.domain.model.Transaction
@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?

    @Query("SELECT * FROM `transaction` WHERE strftime('%Y-%m', timestamp / 1000, 'unixepoch') = strftime('%Y-%m', 'now')")
    fun getTransactionsCreatedOnCurrentMonth(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE strftime('%Y-%m', date / 1000 , 'unixepoch') = strftime('%Y-%m', 'now')")
    fun getTransactionsCreatedForCurrentMonth(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTransactionsForDateRange(startOfMonth: Long, endOfMonth: Long): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<Transaction>)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}