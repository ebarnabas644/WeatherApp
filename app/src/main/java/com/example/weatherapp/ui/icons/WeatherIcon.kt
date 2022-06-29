package com.example.weatherapp.ui.icons

import androidx.annotation.DrawableRes
import com.example.weatherapp.R


public enum class WeatherIcon(@DrawableRes val iconId: Int){
    SNOW(R.drawable.ic_wi_snowflake_cold),
    SNOW_DAY(R.drawable.ic_wi_day_snow),
    SNOW_NIGHT(R.drawable.ic_wi_night_alt_snow),
    THUNDER_RAIN(R.drawable.ic_wi_thunderstorm),
    THUNDER_RAIN_DAY(R.drawable.ic_wi_day_thunderstorm),
    THUNDER_RAIN_NIGHT(R.drawable.ic_wi_night_alt_thunderstorm),
    RAIN(R.drawable.ic_wi_rain),
    RAIN_DAY(R.drawable.ic_wi_day_rain),
    RAIN_NIGHT(R.drawable.ic_wi_night_alt_rain),
    FOG(R.drawable.ic_wi_fog),
    WIND(R.drawable.ic_wi_windy),
    CLOUDY(R.drawable.ic_wi_cloudy),
    CLOUDY_DAY(R.drawable.ic_wi_day_cloudy),
    CLOUDY_NIGHT(R.drawable.ic_wi_night_alt_cloudy),
    DAY(R.drawable.ic_wi_day_sunny),
    NIGHT(R.drawable.ic_wi_night_clear)
}

    public fun ConvertIconToEnum(iconName: String): WeatherIcon{
        return when(iconName){
            "snow" -> WeatherIcon.SNOW
            "snow-showers-day" -> WeatherIcon.SNOW_DAY
            "snow-showers-night" -> WeatherIcon.SNOW_NIGHT
            "thunder-rain" -> WeatherIcon.THUNDER_RAIN
            "thunder-showers-day" -> WeatherIcon.THUNDER_RAIN_DAY
            "thunder-showers-night" -> WeatherIcon.THUNDER_RAIN_NIGHT
            "rain" -> WeatherIcon.RAIN
            "showers-day" -> WeatherIcon.RAIN_DAY
            "showers-night" -> WeatherIcon.RAIN_NIGHT
            "fog" -> WeatherIcon.FOG
            "wind" -> WeatherIcon.WIND
            "cloudy" -> WeatherIcon.CLOUDY
            "partly-cloudy-day" -> WeatherIcon.CLOUDY_DAY
            "partly-cloudy-night" -> WeatherIcon.CLOUDY_NIGHT
            "clear-day" -> WeatherIcon.DAY
            "clear-night" -> WeatherIcon.NIGHT
            else -> WeatherIcon.DAY
        }
    }

//Icon id	Weather Conditions
//snow	Amount of snow is greater than zero
//snow-showers-day	Periods of snow during the day
//snow-showers-night	Periods of snow during the night
//thunder-rain	Thunderstorms throughout the day or night
//thunder-showers-day	Possible thunderstorms throughout the day
//thunder-showers-night	Possible thunderstorms throughout the night
//rain	Amount of rainfall is greater than zero
//showers-day	Rain showers during the day
//showers-night	Rain showers during the night
//fog	Visibility is low (lower than one kilometer or mile)
//wind	Wind speed is high (greater than 30 kph or mph)
//cloudy	Cloud cover is greater than 90% cover
//partly-cloudy-day	Cloud cover is greater than 20% cover during day time.
//partly-cloudy-night	Cloud cover is greater than 20% cover during night time.
//clear-day	Cloud cover is less than 20% cover during day time
//clear-night	Cloud cover is less than 20% cover during night time