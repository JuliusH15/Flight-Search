package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, delete, and retrieve of [Favorite] from a given data source
 */

interface FavoriteRepository {
    /**
     * Insert favorite in the data source
     */
    suspend fun insertFavoriteStream(favorite: Favorite)

    /**
     * Delete favorite from the data source
     */
    suspend fun deleteFavoriteStream(favorite: Favorite)

    /**
     * Retrieve all the favorites from the given data source
     */
    fun getAllFavoritesStream(): Flow<List<Favorite>>

    /**
     * Retrieve a favorite from the data source
     */
    fun getFavoriteStream(departureCode: String, destinationCode: String): Flow<Favorite?>
}