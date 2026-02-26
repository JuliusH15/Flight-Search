package com.example.flightsearch.ui.flights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.model.Flight
import com.example.flightsearch.ui.theme.FlightSearchTheme
import kotlin.text.append

@Composable
fun FlightsList(
    flights: List<Flight>,
    favoriteFlights: List<Favorite>?, // Pass in the list of favorites
    onFavoriteClick: (Flight) -> Unit, // The action to perform on click
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(flights) { flight ->
            // For each flight, check if it exists in the favorite list
            val isFavorite = favoriteFlights?.any { favorite ->
                favorite.departureCode == flight.departureCode && favorite.destinationCode == flight.destinationCode
            }

            FlightCardItem(
                flight = flight,
                isFavorite = isFavorite, // Pass the boolean result
                onIconClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun FlightCardItem(
    flight: Flight,
    isFavorite: Boolean?,
    onIconClick: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(R.string.depart_txt),
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic)
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" " + flight.departureCode)
                        }
                        append(" " + flight.departureName)
                    }
                )
                Text(text = stringResource(R.string.destination_txt),
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic)
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" " + flight.destinationCode)
                        }
                        append(" " + flight.destinationName)
                    }
                )
            }
            IconButton(
                onClick = { onIconClick(flight) },
                modifier = Modifier.weight(.5f)
            ) {
                Icon(
                    imageVector = if (isFavorite == true) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = stringResource(R.string.favorite_button_txt)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FlightsListPreview() {
    FlightSearchTheme {
        FlightsList(
            flights = listOf(
                Flight(departureCode = "SEA", departureName = "Sea Airport", destinationCode = "PLE",
                    destinationName = "PLE Airport"),
                Flight(departureCode = "SEA", departureName = "SEA Airport", destinationCode = "ORD",
                    destinationName = "ORD Airport")
            ),
            favoriteFlights = listOf(
                Favorite(departureCode = "SEA", destinationCode = "PLE")
            ),
            onFavoriteClick = {}
        )
    }
}

@Preview
@Composable
fun FlightCardPreview() {
    FlightSearchTheme {
        FlightCardItem(flight = Flight(departureCode = "SEA", departureName = "Sea Airport", destinationCode = "PLE",
            destinationName = "PLE Airport"),
            isFavorite = true,
            onIconClick = {})
    }
}