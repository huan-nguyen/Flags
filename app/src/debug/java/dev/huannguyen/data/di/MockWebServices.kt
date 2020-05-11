package dev.huannguyen.data.di

import dev.huannguyen.flags.data.FlagApiModel
import dev.huannguyen.flags.data.WebServices
import retrofit2.Response
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit

class MockWebServices(mockRetrofit: MockRetrofit) : WebServices {
    private val delegate: BehaviorDelegate<WebServices> = mockRetrofit.create(WebServices::class.java)

    override suspend fun getFlags(): Response<List<FlagApiModel>> {
        return delegate.returningResponse(
            listOf(
                FlagApiModel(
                    "Australia",
                    "Canberra",
                    24117360,
                    "https://flagpedia.net/data/flags/normal/au.png",
                    "English",
                    "Australian dollar",
                    "UTC+10:00"
                ),
                FlagApiModel(
                    "Finland",
                    "Helsinki",
                    5491817,
                    "https://flagpedia.net/data/flags/normal/fi.png",
                    "Finish",
                    "Euro",
                    "UTC+02:00"
                ),
                FlagApiModel(
                    "France",
                    "Paris",
                    66710000,
                    "https://flagpedia.net/data/flags/normal/fr.png",
                    "French",
                    "Euro",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Germany",
                    "Berlin",
                    81770900,
                    "https://flagpedia.net/data/flags/normal/de.png",
                    "German",
                    "Euro",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Greece",
                    "Athens",
                    10858018,
                    "https://flagpedia.net/data/flags/normal/gr.png",
                    "Greek",
                    "Euro",
                    "UTC+02:00"
                ),
                FlagApiModel(
                    "Hungary",
                    "Budapest",
                    9823000,
                    "https://flagpedia.net/data/flags/normal/hu.png",
                    "Hungarian",
                    "Hungarian forint",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Iceland",
                    "Reykjavík",
                    334300,
                    "https://flagpedia.net/data/flags/normal/is.png",
                    "Icelandic",
                    "Icelandic króna",
                    "UTC+00:00"
                ),
                FlagApiModel(
                    "Ireland",
                    "Dublin",
                    6378000,
                    "https://flagpedia.net/data/flags/normal/ie.png",
                    "Irish",
                    "Euro",
                    "UTC+00:00"
                ),
                FlagApiModel(
                    "Italy",
                    "Rome",
                    60665551,
                    "https://flagpedia.net/data/flags/normal/it.png",
                    "Italian",
                    "Euro",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Luxembourg",
                    "Luxembourg",
                    576200,
                    "https://flagpedia.net/data/flags/normal/lu.png",
                    "Luxembourgish",
                    "Euro",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Netherlands",
                    "Amsterdam",
                    17019800,
                    "https://flagpedia.net/data/flags/normal/nl.png",
                    "Dutch",
                    "Euro",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Norway",
                    "Oslo",
                    5223256,
                    "https://flagpedia.net/data/flags/normal/no.png",
                    "Norwegian",
                    "Norwegian krone",
                    "UTC+01:00"
                ),
                FlagApiModel(
                    "Portugal",
                    "Lisbon",
                    10374822,
                    "https://flagpedia.net/data/flags/normal/pt.png",
                    "Portuguese",
                    "Euro",
                    "UTC+00:00"
                ),
                FlagApiModel(
                    "United Kingdom",
                    "London",
                    65110000,
                    "https://flagpedia.net/data/flags/normal/gb.png",
                    "English",
                    "Pound",
                    "UTC+00:00"
                )
            )
        ).getFlags()
    }
}