package com.example.flightsearch.ui.favorite

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.AppViewModelProvider
import com.example.flightsearch.ui.theme.FlightSearchTheme


@Composable
fun FavoriteList(favoriteList: List<Favorite>,
                 onIconClick: (Favorite) -> Unit,
                 getDepartureName: suspend (Favorite) -> String,
                 getDestinationName: suspend (Favorite) -> String,
                 contentPaddingValues: PaddingValues = PaddingValues(0.dp),
                 modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier,
        contentPadding = contentPaddingValues) {
        items(items = favoriteList, key = { it.id }) { favorite ->
            FavoriteCardItem(
                favorite = favorite,
                onIconClick = onIconClick,
                getDepartureName = getDepartureName,
                getDestinationName = getDestinationName,
                modifier = Modifier
                    .padding(8.dp)
            )

        }
    }


}

@Composable
fun FavoriteCardItem(favorite: Favorite,
                     onIconClick: (Favorite) -> Unit,
                     getDepartureName: suspend (Favorite) -> String,
                     getDestinationName: suspend (Favorite) -> String,
                     modifier: Modifier = Modifier) {
    // 1. Create state to hold the names. Initialize with a placeholder.
    var departureName by remember { mutableStateOf("...") }
    var destinationName by remember { mutableStateOf("...") }

    // 2. Use LaunchedEffect to fetch the names when the composable is first displayed.
    // It will re-run if the departure or destination codes change.
    LaunchedEffect(favorite.departureCode) {
        departureName = getDepartureName(favorite)
    }

    LaunchedEffect(favorite.destinationCode) {
        destinationName = getDestinationName(favorite)
    }

    Card(modifier = modifier.fillMaxWidth()
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
                            append(favorite.departureCode)
                        }
                        append(" " + departureName)
                    }
                )
                Text(text = stringResource(R.string.destination_txt),
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic)
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(favorite.destinationCode)
                        }
                        append(" " + destinationName)
                    }
                )
            }
            IconButton(
                onClick = { onIconClick(favorite) }
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorite_button_txt)
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun FavoriteListPreview() {
    FlightSearchTheme {
        FavoriteList(
            favoriteList = listOf(
                Favorite(id = 1, departureCode = "CAP", destinationCode = "COM"),
                Favorite(id = 2, departureCode = "YES", destinationCode = "RWC")
            ),
            onIconClick = {},
            getDepartureName = {"Cap Airport"},
            getDestinationName = {"Yes Airport"}
        )
    }
}

@Preview
@Composable
fun FavoriteCardItemPreview() {
    FlightSearchTheme {
        FavoriteCardItem(
            favorite = Favorite(id = 1, departureCode = "CAP", destinationCode = "JFK"),
            onIconClick = {},
            getDepartureName = {"Capcom Airport"},
            getDestinationName = {"JFK Airport"}
        )
    }
}