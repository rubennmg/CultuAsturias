package com.example.cultuasturias.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.cultuasturias.databinding.CvItemBinding
import com.example.cultuasturias.model.CulturalVenueItem


class CulturalVenuesListAdapter: ListAdapter<CulturalVenueItem, CulturalVenuesViewHolder>(CulturalVenueItem.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CulturalVenuesViewHolder {
        val itemBinding = CvItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return CulturalVenuesViewHolder(itemBinding.root)
    }

    override fun onBindViewHolder(holder: CulturalVenuesViewHolder, position: Int) {
        val culturalVenueItem = getItem(position)
        holder.bind(culturalVenueItem)
    }
}