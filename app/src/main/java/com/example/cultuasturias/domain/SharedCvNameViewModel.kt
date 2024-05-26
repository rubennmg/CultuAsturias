package com.example.cultuasturias.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedCvNameViewModel : ViewModel() {
    private val _searchName = MutableLiveData<String>()
    val searchName: MutableLiveData<String> get() = _searchName

    fun setSearchName(name: String) {
        _searchName.value = name
    }
}