package com.example.cultuasturias.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel cuyo objetivo es proporcionar el texto de búsqueda introducido por el usuario
 * en la barra de búsqueda de la lista de lugares culturales.
 *
 */
class SharedCvNameViewModel : ViewModel() {
    private val _searchName = MutableLiveData<String>()
    val searchName: MutableLiveData<String> get() = _searchName

    fun setSearchName(name: String) {
        _searchName.value = name
    }
}