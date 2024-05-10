package com.example.cultuasturias.ui

import com.example.cultuasturias.model.CulturalVenuesItem
import com.example.cultuasturias.status.AppStatus

sealed class CultuUIState (val state: AppStatus) {
    data class Success (val datos: List<CulturalVenuesItem>): CultuUIState(AppStatus.SUCCESS)
    data class Error (val message:String): CultuUIState(AppStatus.ERROR)
}