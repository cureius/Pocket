package com.cureius.pocket.feature_account.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cureius.pocket.feature_account.domain.model.Account


@Database(
    entities = [Account::class], version = 1
)
abstract class AccountDatabase : RoomDatabase() {
    abstract val accountDao: AccountDao

    companion object {
        const val DATABASE_NAME = "account_db"
    }
}