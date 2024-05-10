package com.example.cultuasturias.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cultural_venues_table")
data class CulturalVenueItem(
    val AccesoATerrazaOZonaExterior: String,
    val AccesoAlInterior: String,
    val AdmitenOtrasMascotasAdemasDePerros: String,
    val Archivo: String,
    val Canal: String,
    val CodigoPostal: String,
    val Concejo: String,
    val Coordenadas: String,
    val CupoMaximoDePerrosEnElInterior: String,
    val DetalleDeLasNormasEspecificas: String,
    val Direccion: String,
    val Email: String,
    val Facebook: String,
    val FechaDeConstruccion: String,
    val Horario: String,
    val Instagram: String,
    val LimitacionDelTamanopesoDeLaMascota: String,
    val Localidad: String,
    val MasInformacion: String,
    @PrimaryKey val Nombre: String,
    val NumeroMaximoDePerrosPorPersona: String,
    val Observaciones: String,
    val OtrasMascotasPermitidas: String,
    val Pinterest: String,
    val Rss: String,
    val SeAdmitenPppPerrosPotencialmentePeligrosos: String,
    val Slide: String,
    val Tarifas: String,
    val Telefono: String,
    val Texto: String,
    val Titulo: String,
    val Twitter: String,
    val Url: String,
    val Web: String,
    val Youtube: String,
    val Zona: String
)