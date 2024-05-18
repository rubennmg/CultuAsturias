package com.example.cultuasturias.ui

import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.status.AppStatus

sealed class CultuAstUIState (val state: AppStatus) {
    data class Success (val datos: List<CulturalVenueItem>): CultuAstUIState(AppStatus.SUCCESS)
    data class Error (val message:String): CultuAstUIState(AppStatus.ERROR)
}