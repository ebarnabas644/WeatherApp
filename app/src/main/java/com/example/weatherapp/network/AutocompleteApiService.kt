package com.example.weatherapp.network

import com.example.weatherapp.network.dataclass.Predictions
import com.example.weatherapp.network.dataclass.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/"
private const val QUERY_DATA = "json?types=geocode&key=AIzaSyCNMRHO8hSI88ZpBkTa1zPqv30V_5EZT94"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AutocompleteApiService {
    @GET("$QUERY_DATA")
    suspend fun getAutocomplete(@Query("input") address : String): Predictions
}

object AutocompleteApi {
    val retrofitService : AutocompleteApiService by lazy {
        retrofit.create(AutocompleteApiService::class.java)
    }
}