package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport WHERE iata_code LIKE :airportSearch OR " +
            "name LIKE :airportSearch ")
    fun getAirports(airportSearch: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code != :selectedAirport")
    fun getFlights(selectedAirport: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code == :selectedAirport")
    fun getAirportName(selectedAirport: String): Flow<Airport>
}