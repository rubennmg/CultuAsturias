package com.example.cultuasturias.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.CvItemBinding
import com.example.cultuasturias.model.CulturalVenueItem

class CulturalVenuesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemBinding = CvItemBinding.bind(itemView)

    fun bind(item: CulturalVenueItem) {
        val BASE_URL = "https://www.turismoasturiasprofesional.es"
        val slideUrls = item.getSlideUrls()

        if (slideUrls.isNotEmpty()) {
            Glide.with(itemView)
                .load(BASE_URL + slideUrls[0])
                .placeholder(R.drawable.ic_launcher_background)
                .into(itemBinding.venueImage)
        } else {
            itemBinding.venueImage.setImageResource(R.drawable.ic_launcher_background)
        }

        itemBinding.venueName.text = item.Nombre
        itemBinding.venueLocation.text = item.Localidad
    }
}