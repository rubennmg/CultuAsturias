package com.example.cultuasturias.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cultuasturias.data.ApiResult
import com.example.cultuasturias.data.Repository
import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.ui.CultuAstUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class CulturalVenueViewModel : ViewModel() {

    private val _cultuAstUIStateObservable = MutableLiveData<CultuAstUIState>()
    val cultuAstUIStateObservable: LiveData<CultuAstUIState> get() = _cultuAstUIStateObservable

    private val _searchResults = MutableStateFlow<List<CulturalVenueItem>>(emptyList())
    val searchResults: StateFlow<List<CulturalVenueItem>> get() = _searchResults

    init {
        updateCulturalVenuesList()
    }

    fun updateCulturalVenuesList() {
        viewModelScope.launch {
            Repository.updateCulturalVenues().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _cultuAstUIStateObservable.value = CultuAstUIState.Success(result.data!!)
                    }
                    is ApiResult.Error -> {
                        // Si se produce un error al cargar los datos de la API, se cargan los datos de la base de datos
                        loadCulturalVenuesFromDb(result.message!!)
                    }
                }
            }
        }
    }

    fun loadCulturalVenuesFromDb(errorMsg: String) {
        viewModelScope.launch {
            Repository.getAllCulturalVenues().collect { localData ->
                if (localData.isNotEmpty()) {
                    _cultuAstUIStateObservable.value = CultuAstUIState.Success(localData)
                    _cultuAstUIStateObservable.value = CultuAstUIState.Error(errorMsg)
                } else {
                    _cultuAstUIStateObservable.value = CultuAstUIState.Error(errorMsg)
                }
            }
        }
    }

    fun searchCulturalVenues(name: String) {
        viewModelScope.launch {
            Repository.searchCulturalVenues(name)
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }
}