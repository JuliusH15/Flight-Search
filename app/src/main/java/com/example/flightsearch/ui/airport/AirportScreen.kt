package com.example.flightsearch.ui.airport

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.data.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun AirportList(
    airportList: List<Airport>,
    onItemClick: (Airport) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPaddingValues
    ) {
        items(items = airportList, key = { it.id }) { airport ->
            AirportCardItem(
                airport = airport,
                modifier = Modifier
                    .clickable { onItemClick(airport) })

        }

    }

}

@Composable
fun AirportCardItem(
    airport: Airport,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = airport.iataCode,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = airport.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AirportListPreview() {
    FlightSearchTheme {
        AirportList(airportList = listOf(
            Airport(id = 1, name = "Fall Off Airport", iataCode = "OFF", passengers = 25),
            Airport(id = 2, name = "HIP Airport", iataCode = "HIP", passengers = 42)
        ),
            onItemClick = {})
    }
}

@Preview
@Composable
fun AirportCardItemPreview() {
    FlightSearchTheme {
        AirportCardItem(
            airport = Airport(id = 1, name = "HIP Airport", iataCode = "HIP", passengers = 42)
        )
    }
}