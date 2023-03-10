package com.cureius.pocket.feature_account.domain.model

sealed class AccountType(val type: String) {
    data class Savings(val name: String) : AccountType(name)
    data class Current(val name: String) : AccountType(name)
    data class Personal(val name: String) : AccountType(name)
    data class Family(val name: String) : AccountType(name)
    data class Salary(val name: String) : AccountType(name)
}
