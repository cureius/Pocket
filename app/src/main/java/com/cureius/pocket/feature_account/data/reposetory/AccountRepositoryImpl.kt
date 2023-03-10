package com.cureius.pocket.feature_account.data.reposetory

import com.cureius.pocket.feature_account.data.data_source.AccountDao
import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class AccountRepositoryImpl(
    private val dao: AccountDao
) : AccountRepository {
    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
    }

    override suspend fun getAccountById(id: Int): Account? {
        return dao.getAccountById(id)
    }

    override suspend fun insertAccount(account: Account) {
        return dao.insertAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        return dao.deleteAccount(account)
    }
}