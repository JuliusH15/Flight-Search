package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides retrieve of [Airport] from a given data source
 */
interface AirportRepository {
    /**
     * Retrieve airports with text similar to user input
     */
    fun getAirportsStream(airportSearch: String): Flow<List<Airport>>

    /**
     * Retrieve all airports excluding the [selectedAirport]
     */
    fun getFlightsStream(selectedAirport: String): Flow<List<Airport>>

    /**
     * Retrieve airport
     */
    fun getAirportNameStream(selectedAirport: String): Flow<Airport>
}