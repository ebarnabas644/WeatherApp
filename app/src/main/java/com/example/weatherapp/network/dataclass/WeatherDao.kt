package com.example.weatherapp.network.dataclass

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Weather)

    @Update
    suspend fun update(item: Weather)

    @Delete
    suspend fun delete(item: Weather)

    @Query("SELECT * FROM Weather WHERE address = :address LIMIT 1")
    suspend fun findByAddress(address: String): Weather

    @Query("SELECT * from Weather ORDER BY address ASC")
    suspend fun getItems(): List<Weather>
}