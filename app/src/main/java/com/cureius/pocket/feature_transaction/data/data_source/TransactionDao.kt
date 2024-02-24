package com.cureius.pocket.feature_transaction.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.cureius.pocket.feature_transaction.domain.model.Transaction
@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?

    @Query("SELECT * FROM `transaction` WHERE strftime('%Y-%m', event_timestamp / 1000, 'unixepoch') = strftime('%Y-%m', 'now')")
    fun getTransactionsCreatedOnCurrentMonth(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE strftime('%Y-%m', date / 1000 , 'unixepoch') = strftime('%Y-%m', 'now')")
    fun getTransactionsCreatedForCurrentMonth(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :startOfMonth AND :endOfMonth")
    fun getTransactionsForDateRange(startOfMonth: Long, endOfMonth: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE substr(account, -3) IN (:lastThreeDigits)")
    fun getTransactionsOfAccountNumber(lastThreeDigits: Array<String>): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE account = :accountNumber AND balance IS NOT NULL  ORDER BY event_timestamp DESC LIMIT 1")
    fun getLatestTransactionWithBalanceForAccountNumber(accountNumber: String): Flow<Transaction>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransactions(transactions: List<Transaction>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}