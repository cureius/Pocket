package com.cureius.pocket.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.cureius.pocket.feature_account.data.data_source.AccountDatabase
import com.cureius.pocket.feature_account.data.reposetory.AccountRepositoryImpl
import com.cureius.pocket.feature_account.domain.repository.AccountRepository
import com.cureius.pocket.feature_account.domain.use_case.*
import com.cureius.pocket.feature_pot.data.data_source.PotDatabase
import com.cureius.pocket.feature_pot.data.repository.PotRepositoryImpl
import com.cureius.pocket.feature_pot.domain.repository.PotRepository
import com.cureius.pocket.feature_pot.domain.use_case.AddPot
import com.cureius.pocket.feature_pot.domain.use_case.AddPots
import com.cureius.pocket.feature_pot.domain.use_case.DeletePot
import com.cureius.pocket.feature_pot.domain.use_case.GetPot
import com.cureius.pocket.feature_pot.domain.use_case.GetPots
import com.cureius.pocket.feature_pot.domain.use_case.GetPotsWithValidity
import com.cureius.pocket.feature_pot.domain.use_case.GetTemplatePots
import com.cureius.pocket.feature_pot.domain.use_case.PotUseCases
import com.cureius.pocket.feature_pot.domain.use_case.UpdatePot
import com.cureius.pocket.feature_pot.presentation.pots.PotsViewModel
import com.cureius.pocket.feature_transaction.data.data_source.TransactionDatabase
import com.cureius.pocket.feature_transaction.data.repository.TransactionRepositoryImpl
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository
import com.cureius.pocket.feature_transaction.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideTransactionDatabase(app: Application): TransactionDatabase {
        return Room.databaseBuilder(
            app,
            TransactionDatabase::class.java,
            TransactionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: TransactionDatabase): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideTransactionUseCases(repository: TransactionRepository): TransactionUseCases {
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
    fun provideAccountDatabase(app: Application): AccountDatabase {
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


    @Provides
    @Singleton
    fun providePotDatabase(app: Application): PotDatabase {
        return Room.databaseBuilder(
            app,
            PotDatabase::class.java,
            PotDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePotRepository(db: PotDatabase): PotRepository {
        return PotRepositoryImpl(db.potDao)
    }

    @Provides
    @Singleton
    fun providePotUseCases(repository: PotRepository): PotUseCases {
        return PotUseCases(
            getPot = GetPot(repository),
            deletePot = DeletePot(repository),
            addPot = AddPot(repository),
            updatePot = UpdatePot(repository),
            addPots = AddPots(repository),
            getPots = GetPots(repository),
            getTemplatePots = GetTemplatePots(repository),
            getPotsWithValidity = GetPotsWithValidity(repository)
        )
    }

    @Provides
    @Singleton
    fun providePotsViewModel(
        potsUseCases: PotUseCases,
    ): PotsViewModel {
        return PotsViewModel(
            potsUseCases,
        )
    }
}