package com.example.cultuasturias.domain

import android.content.Context
import android.content.SharedPreferences
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

/**
 * ViewModel que se encarga de gestionar los datos de la pantall principal que contiene
 * la lista de CulturalVenueItems.
 *
 */
class CulturalVenueViewModel : ViewModel() {
    private val _cultuAstUIStateObservable = MutableLiveData<CultuAstUIState>()
    val cultuAstUIStateObservable: LiveData<CultuAstUIState> get() = _cultuAstUIStateObservable

    private val _searchResults = MutableStateFlow<List<CulturalVenueItem>>(emptyList())
    val searchResults: StateFlow<List<CulturalVenueItem>> get() = _searchResults

    private var culturalVenues: List<CulturalVenueItem> = emptyList()
    private val _selectedZone = MutableLiveData<String>()
    val selectedZone: MutableLiveData<String> get() = _selectedZone

    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        _selectedZone.value = sharedPreferences.getString("selected_zone", "Todos")
    }


    init {
        updateCulturalVenuesList()
    }

    private fun updateCulturalVenuesList() {
        viewModelScope.launch {
            Repository.updateCulturalVenues().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        culturalVenues = result.data!!
                        applyZoneFilter(_selectedZone.value!!)
                        _cultuAstUIStateObservable.value = CultuAstUIState.Success(culturalVenues)
                    }
                    is ApiResult.Error -> {
                        // Si se produce un error al cargar los datos de la API, se cargan los datos desde la base de datos
                        loadCulturalVenuesFromDb(result.message!!)
                    }
                }
            }
        }
    }

    private fun loadCulturalVenuesFromDb(errorMsg: String) {
        viewModelScope.launch {
            Repository.getAllCulturalVenues().collect { localData ->
                if (localData.isNotEmpty()) {
                    culturalVenues = localData
                    applyZoneFilter(_selectedZone.value!!)
                    _cultuAstUIStateObservable.value = CultuAstUIState.Success(culturalVenues)
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

    fun applyZoneFilter(zone: String) {
        viewModelScope.launch {
            val filteredResults = when (zone) {
                "Todos" -> culturalVenues
                else -> culturalVenues.filter { it.Zona.contains(zone, true) }
            }
            _cultuAstUIStateObservable.value = CultuAstUIState.Success(filteredResults)
        }
    }

    fun clearFilters() {
        _cultuAstUIStateObservable.value = CultuAstUIState.Success(culturalVenues)
    }

    fun setSelectedZone(zone: String) {
        _selectedZone.value = zone
        sharedPreferences.edit().putString("selected_zone", zone).apply()
    }
}