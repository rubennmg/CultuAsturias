package com.example.cultuasturias.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.cultuasturias.data.Repository
import com.example.cultuasturias.model.CulturalVenueItem

class CvItemDetailsViewModel(repository: Repository) : ViewModel() {
    private val cvName = MutableLiveData<String>()

    val mCulturalVenue: LiveData<CulturalVenueItem> = cvName.switchMap {
        name -> repository.getCulturalVenueByName(name).asLiveData()
    }

    fun setCvName(cvname: String) {
        cvName.value = cvname
    }

    class CvItemDetailsViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CvItemDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CvItemDetailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}