package com.example.flightsearch.ui.home

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearch.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(searchQuery = ""))
    val homeUiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Initialize the ViewModel
    init {
        // Use viewModelScope to launch a coroutine
        viewModelScope.launch {
            // Read the initial search query from the repository
            val initialSearchQuery = userPreferencesRepository.userSearchQuery.first() // .first() gets the most recent value and cancels the flow
            _uiState.update { currentState ->
                currentState.copy(searchQuery = initialSearchQuery)
            }
        }
    }

    fun updateSearchQuery(updatedSearchQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = updatedSearchQuery
            )
        }

        // Also, save the new query to the datastore for persistence
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(updatedSearchQuery)
        }
    }

}

data class HomeUiState(val searchQuery: String = "")