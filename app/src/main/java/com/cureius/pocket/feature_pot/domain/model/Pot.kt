package com.cureius.pocket.feature_pot.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["title"], unique = true)])
data class Pot(
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "weight") val weight: Float? = null,
    @ColumnInfo(name = "capacity") val capacity: Double? = null,
    @ColumnInfo(name = "type") val type: String? = "liability",
    @ColumnInfo(name = "filled") val filled: Float? = null,
    @ColumnInfo(name = "is_template") val is_template: Boolean? = false,
    @ColumnInfo(name = "is_monthly") val is_monthly: Boolean? = true,
    @ColumnInfo(name = "is_temporary") val is_temporary: Boolean? = false,
    @ColumnInfo(name = "is_default") val is_default: Boolean? = false,
    @ColumnInfo(name = "validity") val validity: Long? = null,
    @ColumnInfo(name = "associated_account") val associated_account: Int? = null,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "parent") val parent: Int? = null,
    /*--------------For-DB----------------*/
    @ColumnInfo(name = "timestamp") val timestamp: Long? = null,
    @PrimaryKey val id: Int? = null,
)
class InvalidPotException(message: String) : Exception(message)
