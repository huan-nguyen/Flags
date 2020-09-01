package dev.huannguyen.flags.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlagDao {
    @Query("SELECT * FROM LocalFlagData")
    fun getAll(): Flow<List<LocalFlagData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<LocalFlagData>)
}
