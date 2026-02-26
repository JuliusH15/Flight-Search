package com.example.flightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_SEARCH_QUERY = stringPreferencesKey("user_search_query")
        const val TAG = "UserPreferencesRepo"
    }

    val userSearchQuery = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_SEARCH_QUERY] ?: ""
        }

    suspend fun saveSearchQuery(searchQuery: String) {
        dataStore.edit { preferences ->
            preferences[USER_SEARCH_QUERY] = searchQuery
        }
    }
}