package com.example.cultuasturias.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.CulturalVenueItemBinding
import com.example.cultuasturias.model.CulturalVenueItem

class CulturalVenuesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemBinding = CulturalVenueItemBinding.bind(itemView)

    fun bind(item: CulturalVenueItem) {
        val text = itemView.context.getString(
            R.string.itemHolder,
            item.Nombre, item.Concejo, item.Localidad)

        itemBinding.text.text = text
    }
}