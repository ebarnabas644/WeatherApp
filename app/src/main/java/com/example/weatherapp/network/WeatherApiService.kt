package com.example.weatherapp.network

import com.example.weatherapp.network.dataclass.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL =
    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
private const val QUERY_DATA = "?unitGroup=metric&elements=datetime%2CdatetimeEpoch%2Caddress%2CresolvedAddress%2Clatitude%2Clongitude%2Ctempmax%2Ctempmin%2Ctemp%2Cfeelslike%2Chumidity%2Cwindspeed%2Cwinddir%2Cuvindex%2Csunrise%2Csunset%2Cdescription%2Cicon&key=2YM68T2QCXTW94ESQVKNGJ4CJ&contentType=json"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {
    @GET("{address}$QUERY_DATA")
    suspend fun getWeather(@Path("address") address : String): Weather
}

object WeatherApi {
    val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}