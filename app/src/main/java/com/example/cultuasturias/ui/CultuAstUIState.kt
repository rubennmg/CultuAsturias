package com.example.cultuasturias.ui

import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.status.AppStatus

/**
 * Clase que representa el estado de la UI de la pantalla principal de la aplicación.
 * Puede ser un estado de éxito o un estado de error.
 *
 */
sealed class CultuAstUIState (val state: AppStatus) {
    data class Success (val datos: List<CulturalVenueItem>): CultuAstUIState(AppStatus.SUCCESS)
    data class Error (val message:String): CultuAstUIState(AppStatus.ERROR)
}