package com.example.weatherapp.network.dataclass

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//https://www.answertopia.com/jetpack-compose/a-jetpack-compose-room-database-and-repository-tutorial/
@Database(entities = [Weather::class], version = 1, exportSchema = false)
@TypeConverters(WeatherAlertTypeConverters::class, WeatherForecastDayTypeConverters::class, WeatherHourTypeConverters::class, WeatherCurrentDayTypeConverters::class)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherRoomDatabase? = null
        fun getDatabase(context: Context): WeatherRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDatabase::class.java,
                    "weather_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}