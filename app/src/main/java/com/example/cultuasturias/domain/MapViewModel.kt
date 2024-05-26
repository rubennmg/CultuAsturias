package com.example.cultuasturias.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cultuasturias.data.Repository
import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.ui.CultuAstUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _mapUIStateObservable = MutableLiveData<CultuAstUIState>()
    val mapUIStateObservable: LiveData<CultuAstUIState> get() = _mapUIStateObservable

    private val _culturalVenues = MutableStateFlow<List<CulturalVenueItem>>(emptyList())
    val culturalVenues: StateFlow<List<CulturalVenueItem>> get() = _culturalVenues

    init {
        loadCulturalVenues()
    }

    private fun loadCulturalVenues() {
        viewModelScope.launch {
            Repository.getAllCulturalVenues().collect { localData ->
                if (localData.isNotEmpty()) {
                    _mapUIStateObservable.value = CultuAstUIState.Success(localData)
                } else {
                    _mapUIStateObservable.value = CultuAstUIState.Error("No se han encontrado datos") // i18n
                }
            }
        }
    }
}