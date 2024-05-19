package com.example.cultuasturias.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedCvNameViewModel : ViewModel() {
    private val _searchName = MutableLiveData<String>()
    val searchName: MutableLiveData<String> get() = _searchName

    fun setSearchName(name: String) {
        _searchName.value = name
    }
}