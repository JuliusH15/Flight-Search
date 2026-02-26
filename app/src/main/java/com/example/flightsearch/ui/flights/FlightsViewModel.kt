package com.example.flightsearch.ui.flights

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportRepository
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FavoriteRepository
import com.example.flightsearch.model.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightsViewModel(private val airportRepository: AirportRepository, private val favoriteRepository: FavoriteRepository): ViewModel() {
    private val _uiState = MutableStateFlow(FlightsUiState(flightsList = listOf(), selectedAirport = ""))
    val flightsUiState: StateFlow<FlightsUiState> = _uiState.asStateFlow()

    fun getFlightsList(selectedAirportCode: String) {
        // Use viewModelScope to launch a coroutine to safely collect from the Flow
        viewModelScope.launch {
            // Get the Flow<List<Airport>> from the repository
            airportRepository.getFlightsStream(selectedAirportCode)
                // The 'collect' operator will wait for the Flow to emit its value
                .collect { destinationAirports ->
                    // Inside this block, 'destinationAirports' is a regular List<Airport>

                    // Now, create the list of Flight objects
                    val flights = destinationAirports.map { destinationAirport ->
                        Flight(
                            departureCode = selectedAirportCode,
                            departureName = getAirportName(selectedAirportCode),
                            destinationCode = destinationAirport.iataCode,// Assuming you want the code
                            destinationName = getAirportName(destinationAirport.iataCode))
                    }

                    // Finally, update the UI state with the new list
                    _uiState.update { currentState ->
                        currentState.copy(
                            flightsList = flights,
                            selectedAirport = selectedAirportCode
                        )
                    }
                }
        }
    }

    /**
     * Resets the UI state to clear the selected airport and flight list.
     * This is called when the user starts a new search.
     */
    fun clearSelection() {
        _uiState.update {
            it.copy(
                flightsList = emptyList(),
                selectedAirport = "" // Resetting to an empty string
            )
        }
    }

    suspend fun getAirportName(airportCode: String): String {
        val airport = airportRepository.getAirportNameStream(airportCode).first()
        return airport.name
    }

    suspend fun toggleFavorite(flight: Flight) {
        // First, check if this flight already exists as a favorite in the database.
        // We need a way to check this from the repository. Let's assume we have a getFavoriteStream() method.
        val existingFavorite: Favorite? = favoriteRepository
            .getFavoriteStream(flight.departureCode, flight.destinationCode)
            .first() // .first() gets the first emission, which might be a Favorite or null

        if (existingFavorite == null) {
            // If it doesn't exist, it's not a favorite yet. Add it.
            addFavorite(flight)
        } else {
            // If it exists, it's already a favorite. Delete it.
            // The deleteFavorite method needs the Favorite object, which we just retrieved.
            deleteFavorite(existingFavorite)
        }
    }

    suspend fun addFavorite(flight: Flight) {
        val favorite = Favorite(departureCode = flight.departureCode, destinationCode = flight.destinationCode)
        favoriteRepository.insertFavoriteStream(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        favoriteRepository.deleteFavoriteStream(favorite)
    }
}

data class FlightsUiState(val flightsList: List<Flight> = listOf(),
                          val selectedAirport: String = "")