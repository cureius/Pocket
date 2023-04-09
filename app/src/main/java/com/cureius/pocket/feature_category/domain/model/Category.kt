package com.cureius.pocket.feature_category.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["title"], unique = true)])
data class Category(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "filled") val filled: Float? = null,
    /*--------------For-DB----------------*/
    @ColumnInfo(name = "timestamp") val timestamp: Long? = null,
    @PrimaryKey val id: Int? = null,
)

class InvalidCategoryException(message: String) : Exception(message)
