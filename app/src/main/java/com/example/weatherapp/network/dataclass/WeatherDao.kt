package com.example.weatherapp.network.dataclass

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

//https://www.answertopia.com/jetpack-compose/a-jetpack-compose-room-database-and-repository-tutorial/
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Weather)

    @Update
    fun update(item: Weather)

    @Query("DELETE FROM Weather WHERE address = :address")
    suspend fun delete(address: String)

    @Query("SELECT * FROM Weather WHERE address = :address LIMIT 1")
    suspend fun findByAddress(address: String): Weather

    @Query("SELECT * from Weather ORDER BY address ASC")
    fun getItems(): Flow<List<Weather>>

    @Query("SELECT address from Weather ORDER BY address ASC")
    fun getAddresses(): Flow<List<String>>
}