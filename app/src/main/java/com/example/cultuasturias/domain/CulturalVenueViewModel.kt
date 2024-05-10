package com.example.cultuasturias.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cultuasturias.data.ApiResult
import com.example.cultuasturias.data.Repository.updateCulturalVenues
import com.example.cultuasturias.ui.CultuUIState
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

class CulturalVenueViewModel : ViewModel() {
    private val _cultuUIStateObservable = MutableLiveData<CultuUIState>()
    val stopsUIStateObservable: LiveData<CultuUIState> get() = _cultuUIStateObservable

    fun getCulturalVenuesList() {
        viewModelScope.launch {
            updateCulturalVenues().map { result ->
                Log.d("CulturalVenuesViewModel", "updateCulturalVenues: $result")
                Log.d("CulturalVenuesViewModel", "updateCulturalVenues: ${result.data?.size}")
                when (result) {
                    is ApiResult.Success ->  CultuUIState.Success(result._data)
                    is ApiResult.Error -> CultuUIState.Error(result.message!!)
                }
            }.collect {
                _cultuUIStateObservable.value = it
            }
        }
    }

    init {
        getCulturalVenuesList()
    }
}