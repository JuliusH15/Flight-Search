package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.model.Flight
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorite ORDER BY departure_code ASC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE departure_code == :departureCode AND destination_code == :destinationCode")
    fun getFavorite(departureCode: String, destinationCode: String): Flow<Favorite?>
}