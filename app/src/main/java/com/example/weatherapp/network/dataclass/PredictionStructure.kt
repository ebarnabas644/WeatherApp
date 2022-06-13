package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class PredictionStructure(
    @Json(name = "main_text") val main: String = "",
    @Json(name = "secondary_text") val secondPart: String = ""
)
