/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchPlaceBinding

const val SEARCH_RESULT_PLACE_LAYOUT = R.layout.search_place

class SearchPlaceViewHolder(private val binding: SearchPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(place: SearchResult.PlaceItem, onClick: (searchResult: SearchResult) -> Unit) {
        binding.place = place

        itemView.setOnClickListener {
            onClick(place)
        }
    }
}