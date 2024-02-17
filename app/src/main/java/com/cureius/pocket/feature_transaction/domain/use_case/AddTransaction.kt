package com.cureius.pocket.feature_transaction.domain.use_case

import com.cureius.pocket.feature_transaction.domain.model.InvalidTransactionException
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import com.cureius.pocket.feature_transaction.domain.repository.TransactionRepository

class AddTransaction(
    private val repository: TransactionRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transaction: Transaction) {
        if (transaction.title?.isBlank() == true) {
            throw InvalidTransactionException("The Title of the Transaction cant be empty")
        }
        if (transaction.type?.isBlank() == true) {
            throw InvalidTransactionException("The Type of the Transaction cant be empty")
        }
//        if (transaction.account.isBlank()) {
//            throw InvalidTransactionException("The Account of the Transaction cant be empty")
//        }
        if (transaction.amount?.isNaN() == true) {
            throw InvalidTransactionException("The Amount of the Transaction cant be empty")
        }
        if (transaction.date?.equals(null) == true) {
            throw InvalidTransactionException("The Date of the Transaction cant be empty")
        }
//        if (transaction.balance.equals(null)) {
//            throw InvalidTransactionException("The Balance of the Transaction cant be empty")
//        }
        repository.insertTransaction(transaction)
    }

}