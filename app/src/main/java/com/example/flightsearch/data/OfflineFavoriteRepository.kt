package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoriteRepository(private val favoriteDao: FavoriteDao): FavoriteRepository {
    override suspend fun insertFavoriteStream(favorite: Favorite) = favoriteDao.insert(favorite)

    override suspend fun deleteFavoriteStream(favorite: Favorite) = favoriteDao.delete(favorite)

    override fun getAllFavoritesStream(): Flow<List<Favorite>> = favoriteDao.getAllFavorites()

    override fun getFavoriteStream(departureCode: String, destinationCode: String): Flow<Favorite?> =
        favoriteDao.getFavorite(departureCode = departureCode, destinationCode = destinationCode)
}