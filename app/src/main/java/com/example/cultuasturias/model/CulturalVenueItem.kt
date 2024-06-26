package com.example.cultuasturias.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

/**
 * Clase que representa un elemento de la lista de espacios culturales
 * Autogenerada con la herramienta jsonTokotlin
 * Añadidos métodos para obtener las URLs de las imágenes y las coordenadas
 * de cada espacio cultural
 *
 */
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
{
    companion object DIFF_CALLBACK: DiffUtil.ItemCallback<CulturalVenueItem>() {
        override fun areItemsTheSame(oldItem: CulturalVenueItem, newItem: CulturalVenueItem): Boolean {
            return oldItem.Nombre == newItem.Nombre
        }

        override fun areContentsTheSame(oldItem: CulturalVenueItem, newItem: CulturalVenueItem): Boolean {
            return oldItem == newItem
        }
    }

    // Obtener lista de URLs de imágenes a partir del campo Slide
    fun getSlideUrls(): List<String> {
        return if (Slide.isNotBlank()) {
            Slide.split(",")
        } else {
            emptyList()
        }
    }

    // Obtener par de coordenadas a partir del campo Coordenadas
    fun getCoords(): LatLng {
        val coords = Coordenadas.split(",")
        return if (coords.size == 2) {
            LatLng(coords[0].toDouble(), coords[1].toDouble())
        } else {
            LatLng(0.0, 0.0)
        }
    }
}