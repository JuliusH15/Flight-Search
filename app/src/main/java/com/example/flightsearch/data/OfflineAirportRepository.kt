package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository(private val airportDao: AirportDao): AirportRepository {
    override fun getAirportsStream(airportSearch: String): Flow<List<Airport>> = airportDao.getAirports("%"+airportSearch+"%")

    override fun getFlightsStream(selectedAirport: String): Flow<List<Airport>> = airportDao.getFlights(selectedAirport)

    override fun getAirportNameStream(selectedAirport: String): Flow<Airport> = airportDao.getAirportName(selectedAirport)

}