package com.example.flightsearch.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository,
                        private val airportRepository: AirportRepository): ViewModel() {
    val favoriteUiState: StateFlow<FavoriteUiState> =
        favoriteRepository.getAllFavoritesStream().map { FavoriteUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavoriteUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        favoriteRepository.deleteFavoriteStream(favorite)
    }

    suspend fun getFavoriteDepartureName(favorite: Favorite): String {
        val airport = airportRepository.getAirportNameStream(favorite.departureCode).first()
        return airport.name
    }

    suspend fun getFavoriteDestinationName(favorite: Favorite): String {
        val airport = airportRepository.getAirportNameStream(favorite.destinationCode).first()
        return airport.name
    }
}

data class FavoriteUiState(val favoritesList: List<Favorite> = listOf())