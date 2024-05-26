package com.example.cultuasturias.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.CvItemBinding
import com.example.cultuasturias.model.CulturalVenueItem

class CulturalVenuesViewHolder(private val itemBinding: CvItemBinding, private  val onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemBinding.root) {
    init {
        itemView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }

    fun bind(item: CulturalVenueItem) {
        val BASE_URL = "https://www.turismoasturiasprofesional.es"
        val slideUrls = item.getSlideUrls()

        if (slideUrls.isNotEmpty()) {
            Glide.with(itemView)
                .load(BASE_URL + slideUrls[0])
                .override(200, 200) // redimensionar la imagen
                .placeholder(R.drawable.ic_launcher_background)
                .into(itemBinding.venueImage)
        } else {
            itemBinding.venueImage.setImageResource(R.drawable.ic_launcher_background)
        }

        itemBinding.venueName.text = item.Nombre
        itemBinding.venueLocation.text = item.Localidad
    }
}