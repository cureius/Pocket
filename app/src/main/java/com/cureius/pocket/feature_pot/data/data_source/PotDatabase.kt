package com.cureius.pocket.feature_pot.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.util.ImageTypeConverter

@Database(
    entities = [Pot::class], version = 2, exportSchema = true
)
@TypeConverters(ImageTypeConverter::class)
abstract class PotDatabase : RoomDatabase() {
    abstract val potDao: PotDao

    companion object {
        const val DATABASE_NAME = "pot_db"
    }

}