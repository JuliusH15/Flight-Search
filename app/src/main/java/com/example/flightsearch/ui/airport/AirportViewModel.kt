package com.example.flightsearch.ui.airport

import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.ui.favorite.FavoriteUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AirportViewModel(private val airportRepository: AirportRepository): ViewModel() {
    // 1. Private mutable StateFlow to hold the entire UI state.
    //    This is the "source of truth" for the screen.
    private val _airportUiState = MutableStateFlow(AirportUiState())
    val airportUiState: StateFlow<AirportUiState> = _airportUiState.asStateFlow()

    // We still need to track the search query. A simple property is fine.
    private var searchQuery: String = ""

    // 2. A public method for the UI to call whenever the user types.
    //    This is the main entry point for the logic.
    fun onSearchQueryChange(newQuery: String) {
        // Update the internally tracked query
        searchQuery = newQuery

        // If the new query is empty, clear the list and return.
        if (newQuery.isBlank()) {
            _airportUiState.update { it.copy(airportList = emptyList()) }
            return
        }

        // 3. Launch a coroutine to fetch the data asynchronously.
        //    This is the key pattern from Unit 6.
        viewModelScope.launch {
            airportRepository.getAirportsStream(searchQuery)
                .collect { matchingAirports ->
                    // 4. Once the data arrives, update the UI state.
                    _airportUiState.update { currentState ->
                        // If matchingAirports is not null, use it.
                        // If matchingAirports IS null, use an emptyList() instead.
                        currentState.copy(airportList = matchingAirports ?: emptyList())
                    }
                }
        }
    }
}

data class AirportUiState(val airportList: List<Airport> = listOf())