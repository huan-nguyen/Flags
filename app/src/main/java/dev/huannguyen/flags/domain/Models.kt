package dev.huannguyen.flags.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flag(
    val country: String,
    val capital: String,
    val population: Int,
    val url: String,
    val language: String,
    val currency: String,
    val timeZone: String
) : Parcelable
