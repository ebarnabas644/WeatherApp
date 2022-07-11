package com.example.weatherapp.network

import com.example.weatherapp.network.dataclass.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// API parameters.
private const val BASE_URL =
    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
private const val QUERY_DATA = "?unitGroup=metric&elements=datetime%2CdatetimeEpoch%2Caddress%2CresolvedAddress%2Clatitude%2Clongitude%2Ctempmax%2Ctempmin%2Ctemp%2Cfeelslike%2Chumidity%2Cwindspeed%2Cwinddir%2Cuvindex%2Csunrise%2Csunset%2Cdescription%2Cicon&include=days%2Chours%2Cfcst%2Cremote%2Cobs%2Calerts%2Ccurrent&contentType=json"
private const val API_KEY = "&key="  //Visual crossing weather api key

// Setting up moshi with json to kotlin class converter.
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Setting up retrofit with moshi.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Defining http calls.
interface WeatherApiService {

    // Get [address] weather data.
    @GET("{address}$QUERY_DATA$API_KEY")
    suspend fun getWeather(@Path("address") address : String): Weather
}

object WeatherApi {
    val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}