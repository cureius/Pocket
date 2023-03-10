package com.cureius.pocket.feature_account.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["account_number", "card_number"], unique = true)])
data class Account(
    @ColumnInfo(name = "account_number") val account_number: String,
    @ColumnInfo(name = "bank") val bank: String,
    @ColumnInfo(name = "holder_name") val holder_name: String? = null,
    @ColumnInfo(name = "card_number") val card_number: String? = null,
    @ColumnInfo(name = "account_type") val account_type: String? = "savings",
    @ColumnInfo(name = "is_primary") val is_primary: Boolean = false,
    /*--------------For-DB----------------*/
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @PrimaryKey val id: Int? = null,
)

class InvalidAccountException(message: String) : Exception(message)
