package dev.huannguyen.flags.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalFlagData::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun flagDao(): FlagDao
}
