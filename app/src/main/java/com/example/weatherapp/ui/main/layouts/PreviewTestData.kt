package com.example.weatherapp.ui.main.layouts

import com.example.weatherapp.network.dataclass.WeatherForecastDay
import com.example.weatherapp.network.dataclass.WeatherHour
import java.text.DecimalFormat

val weekForecastTestDatasource = List(14) { i ->
    WeatherForecastDay(
        datetime = "2021-05-${"%02d".format(i+1)}",
        datetimeEpoch = 1654034400 + i * 86400,
        tempMax = 20.1,
        tempMin = 12.2,
        avgTemp = 15.2,
        feelsLike = 14.0,
        avgHumidity = 10.2,
        avgWindSpeed = 30.1,
        avgWindDirection = 270.0,
        avgUvIndex = 6.0,
        sunrise = "05:05:12",
        sunset = "21:40:43",
        description = "Partly cloudy throughout the day with a chance of rain throughout the day.",
        imageIcon = "rain",
        hours = listOf()
    )
}

val dayForecastTestDatasource = List(24){ i ->
    WeatherHour(
        datetime = "${"%02d".format(i)}:00:00",
        datetimeEpoch = 1654034400 + i * 3600,
        temp = 8.3,
        feelsLike = 7.9,
        humidity = 15.2,
        windSpeed = 21.0,
        windDirection = 120.4,
        uvIndex = 7.5,
        imageIcon = "cloudy"
    )
}