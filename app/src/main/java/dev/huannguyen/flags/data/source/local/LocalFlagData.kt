package dev.huannguyen.flags.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalFlagData(
    @PrimaryKey val country: String,
    val capital: String,
    val population: Int,
    val url: String,
    val language: String,
    val currency: String,
    val timeZone: String
)
