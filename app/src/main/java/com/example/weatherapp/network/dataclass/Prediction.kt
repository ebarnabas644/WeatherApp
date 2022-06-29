package com.example.weatherapp.network.dataclass

import com.squareup.moshi.Json

data class Prediction(
    val description: String = "",
    @Json(name = "structured_formatting") val structure: PredictionStructure = PredictionStructure()
)
