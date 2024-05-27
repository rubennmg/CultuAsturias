package com.example.cultuasturias.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel compartido entre la pantalla principal y la pantalla de detalles de un CulturalVenueItem.
 * Se utiliza para compartir el nombre de un CulturalVenueItem entre ambos fragmentos
 *
 */
class SharedCvNameViewModel : ViewModel() {
    private val _searchName = MutableLiveData<String>()
    val searchName: MutableLiveData<String> get() = _searchName

    fun setSearchName(name: String) {
        _searchName.value = name
    }
}