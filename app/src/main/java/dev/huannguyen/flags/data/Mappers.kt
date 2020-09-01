package dev.huannguyen.flags.data

import dev.huannguyen.flags.data.source.local.LocalFlagData
import dev.huannguyen.flags.data.source.remote.RemoteFlagData

fun RemoteFlagData.toLocalData() = LocalFlagData(
    country, capital, population, url, language, currency, timeZone
)
