package com.example.flightsearch.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.ui.airport.AirportViewModel
import com.example.flightsearch.ui.favorite.FavoriteViewModel
import com.example.flightsearch.ui.flights.FlightsViewModel
import com.example.flightsearch.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire FlightSearch app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AirportViewModel
        initializer {
            AirportViewModel(
                flightsSearchApplication().container.airportRepository
            )
        }

        initializer {
            HomeViewModel(userPreferencesRepository = flightsSearchApplication().container.userPreferencesRepository)
        }
        // Initializer for FavoriteViewModel
        initializer {
            FavoriteViewModel(favoriteRepository = flightsSearchApplication().container.favoriteRepository,
                airportRepository = flightsSearchApplication().container.airportRepository)
        }

        // Initializer for FlightsViewModel
        initializer {
            FlightsViewModel(
                airportRepository = flightsSearchApplication().container.airportRepository,
                favoriteRepository = flightsSearchApplication().container.favoriteRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlightSearchApplication].
 */
fun CreationExtras.flightsSearchApplication(): FlightSearchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
