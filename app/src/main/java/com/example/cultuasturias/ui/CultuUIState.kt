package com.example.cultuasturias.ui

import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.status.AppStatus

sealed class CultuUIState (val state: AppStatus) {
    data class Success (val datos: List<CulturalVenueItem>): CultuUIState(AppStatus.SUCCESS)
    data class Error (val message:String): CultuUIState(AppStatus.ERROR)
}