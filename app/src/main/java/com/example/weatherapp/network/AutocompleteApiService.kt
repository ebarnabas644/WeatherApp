package com.example.weatherapp.network

import com.example.weatherapp.network.dataclass.Predictions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/"
private const val QUERY_DATA = "json?types=geocode"
private const val API_KEY = "&key="  //Google autocomplete api key here
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AutocompleteApiService {
    @GET("$QUERY_DATA$API_KEY")
    suspend fun getAutocomplete(@Query("input") address : String): Predictions
}

object AutocompleteApi {
    val retrofitService : AutocompleteApiService by lazy {
        retrofit.create(AutocompleteApiService::class.java)
    }
}