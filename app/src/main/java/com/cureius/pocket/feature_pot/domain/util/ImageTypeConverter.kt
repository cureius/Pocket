package com.cureius.pocket.feature_pot.domain.util

import androidx.room.TypeConverter

class ImageTypeConverter {
    @TypeConverter
    fun fromImageVector(imageVectorId: Int): String {
        return IconDictionary.allIcons.filterValues { it == imageVectorId }.keys.elementAt(0)
    }

    @TypeConverter
    fun toImageVector(data: String): Int? {
        return IconDictionary.allIcons[data]
    }
}