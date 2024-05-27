package com.example.cultuasturias.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.SliderItemBinding
import com.smarteist.autoimageslider.SliderViewAdapter

/**
 * Clase que representa el adaptador para el slider de imágenes de la vista de detalles de un CulturalVenueItem.
 * La carga de imágenes se hace con Glide.
 *
 * @param imageUrl Lista de URLs de las imágenes a mostrar.
 */
class ImgSliderAdapter(private val imageUrl: ArrayList<String>) : SliderViewAdapter<ImgSliderAdapter.SliderViewHolder>() {
    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val binding = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        viewHolder.bind(imageUrl[position])
    }

    class SliderViewHolder(private val binding: SliderItemBinding) : SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            val BASE_URL = "https://www.turismoasturiasprofesional.es"

            Glide.with(binding.root)
                .load(BASE_URL + imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.sliderImage)
        }
    }
}