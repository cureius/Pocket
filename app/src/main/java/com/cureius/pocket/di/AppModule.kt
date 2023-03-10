package com.cureius.pocket.di

import android.app.Application
import androidx.room.Room
import com.cureius.pocket.feature_account.data.data_source.AccountDatabase
import com.cureius.pocket.feature_account.data.reposetory.AccountRepositoryImpl
import com.cureius.pocket.feature_account.domain.repository.AccountRepository
import com.cureius.pocket.feature_account.domain.use_case.*
import com.cureius.pocket.feature_transaction.data.data_source.TransactionDatabase
import com.cureius.pocket.feature_transaction.data.repository.TransactionRepositoryImpl
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository
import com.cureius.pocket.feature_transaction.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTransactionDatabase(app: Application): TransactionDatabase{
        return Room.databaseBuilder(
            app,
            TransactionDatabase::class.java,
            TransactionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: TransactionDatabase): TransactionRepository{
        return TransactionRepositoryImpl(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideTransactionUseCases(repository: TransactionRepository): TransactionUseCases{
        return TransactionUseCases(
            getTransaction = GetTransaction(repository),
            deleteTransaction = DeleteTransaction(repository),
            addTransaction = AddTransaction(repository),
            addTransactions = AddTransactions(repository),
            getTransactions = GetTransactions(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAccountDatabase(app: Application): AccountDatabase{
        return Room.databaseBuilder(
            app,
            AccountDatabase::class.java,
            AccountDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAccountRepository(db: AccountDatabase): AccountRepository {
        return AccountRepositoryImpl(db.accountDao)
    }

    @Provides
    @Singleton
    fun provideAccountUseCases(repository: AccountRepository): AccountUseCases {
        return AccountUseCases(
            getAccount = GetAccount(repository),
            deleteAccount = DeleteAccount(repository),
            addAccount = AddAccount(repository),
            getAccounts = GetAccounts(repository)
        )
    }
}