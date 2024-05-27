package com.example.cultuasturias.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.cultuasturias.databinding.CvItemBinding
import com.example.cultuasturias.model.CulturalVenueItem

/**
 * Clase que representa el adaptador para la lista principal de lugares culturales.
 *
 * @param onNameselected Función que se ejecuta al hacer click en un elemento de la lista.
 */
class CulturalVenuesListAdapter(private val onNameselected: (String) -> Unit) : ListAdapter<CulturalVenueItem, CulturalVenuesViewHolder>(CulturalVenueItem.DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CulturalVenuesViewHolder {
        val itemBinding = CvItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return CulturalVenuesViewHolder(itemBinding) {
            onNameselected(getItem(it).Nombre)
        }
    }

    override fun onBindViewHolder(holder: CulturalVenuesViewHolder, position: Int) {
        val culturalVenueItem = getItem(position)
        holder.bind(culturalVenueItem)
    }
}