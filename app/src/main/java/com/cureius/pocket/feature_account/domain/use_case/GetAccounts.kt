package com.cureius.pocket.feature_account.domain.use_case

import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAccounts(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getAccounts()
    }
}