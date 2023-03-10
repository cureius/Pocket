package com.cureius.pocket.feature_account.domain.use_case

import com.cureius.pocket.feature_account.domain.model.Account
import com.cureius.pocket.feature_account.domain.model.InvalidAccountException
import com.cureius.pocket.feature_account.domain.repository.AccountRepository
import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException

class AddAccount(
    private val repository: AccountRepository
) {
    @Throws(InvalidAccountException::class)
    suspend operator fun invoke(account: Account) {
        if (account.bank.isBlank()) {
            throw InvalidTransactionException("Require A Valid Bank")
        }
        if (account.account_number.isBlank()) {
            throw InvalidTransactionException("Require A Valid Account Number")
        }

        repository.insertAccount(account)
    }
}