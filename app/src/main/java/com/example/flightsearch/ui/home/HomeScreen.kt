package com.example.flightsearch.ui.home

import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.airport.AirportList
import com.example.flightsearch.ui.airport.AirportViewModel
import com.example.flightsearch.ui.favorite.FavoriteList
import com.example.flightsearch.ui.favorite.FavoriteViewModel
import com.example.flightsearch.ui.flights.FlightsList
import com.example.flightsearch.ui.flights.FlightsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    airportViewModel: AirportViewModel = viewModel(factory = AppViewModelProvider.Factory),
    favoriteViewModel: FavoriteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    flightsViewModel: FlightsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val airportUiState by airportViewModel.airportUiState.collectAsState()
    val favoriteUiState by favoriteViewModel.favoriteUiState.collectAsState()
    val flightsUiState by flightsViewModel.flightsUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { FlightScreenTopAppBar(
            title = stringResource(R.string.app_name),
            scrollBehavior = scrollBehavior
        )}
    ) { innerPadding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = homeUiState.searchQuery,
                label = { Text(text = "Enter departure airport")},
                onValueChange = {newQuery ->
                    homeViewModel.updateSearchQuery(newQuery)
                    flightsViewModel.clearSelection()
                    airportViewModel.onSearchQueryChange(newQuery)
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null)}
            )
            if (flightsUiState.selectedAirport.isNotBlank()) {
                //Show list of flights
                Column {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.flights_txt))
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" " + flightsUiState.selectedAirport)
                        } })
                    FlightsList(
                        flights = flightsUiState.flightsList,
                        favoriteFlights = favoriteUiState.favoritesList,
                        onFavoriteClick = { flight ->
                            coroutineScope.launch {
                                flightsViewModel.toggleFavorite(flight)
                            }
                        },
                        contentPaddingValues = innerPadding
                    )
                }
            }
            else if(homeUiState.searchQuery.isNotBlank()) {
                //Show list of airports
                AirportList(
                    airportList = airportUiState.airportList,
                    onItemClick = { airport ->
                        flightsViewModel.getFlightsList(airport.iataCode)
                    },
                    contentPaddingValues = innerPadding
                )
            }
            else {
                //Show list of favorite routes
                Column {
                    Text(text = stringResource(R.string.favorite_routes_txt),
                        fontWeight = FontWeight.Bold)

                    FavoriteList(
                        favoriteList = favoriteUiState.favoritesList,
                        onIconClick = { favorite ->
                            coroutineScope.launch {
                                favoriteViewModel.deleteFavorite(favorite)
                            }
                        },
                        getDepartureName = { favorite ->
                            favoriteViewModel.getFavoriteDepartureName(favorite)
                        },
                        getDestinationName = { favorite ->
                            favoriteViewModel.getFavoriteDestinationName(favorite)
                        },
                        contentPaddingValues = innerPadding
                    )
                }

            }
        }

    }
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightScreenTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(title = { Text(text = title, color = Color.White) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Cyan // Example: Change to any color you like
        ))
}