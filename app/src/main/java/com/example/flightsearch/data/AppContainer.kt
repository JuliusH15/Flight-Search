package com.example.flightsearch.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val airportRepository: AirportRepository
    val favoriteRepository: FavoriteRepository
    val userPreferencesRepository: UserPreferencesRepository
}

// Top-level property delegate to create a DataStore instance
private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

/**
 * [AppContainer] implementation that provides instances of [AirportRepository] and [FavoriteRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [AirportRepository] and [FavoriteRepository]
     */
    override val airportRepository: AirportRepository by lazy {
        OfflineAirportRepository(FlightsDatabase.getDatabase(context).airportDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        OfflineFavoriteRepository(FlightsDatabase.getDatabase(context).favoriteDao())
    }

    /**
     * Implementation for [UserPreferencesRepository]
     */
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
}