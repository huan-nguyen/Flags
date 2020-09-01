package dev.huannguyen.flags.data.source.local

import kotlinx.coroutines.flow.Flow

interface LocalFlagDataSource {
    fun getFlags(): Flow<List<LocalFlagData>>

    fun saveFlags(data: List<LocalFlagData>)
}

class LocalFlagDataSourceImpl(private val database: Database) : LocalFlagDataSource {
    override fun getFlags(): Flow<List<LocalFlagData>> = database.flagDao().getAll()

    override fun saveFlags(data: List<LocalFlagData>) {
        database.flagDao().insertAll(data)
    }
}
