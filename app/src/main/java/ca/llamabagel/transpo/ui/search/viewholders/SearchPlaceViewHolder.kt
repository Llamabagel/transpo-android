/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchPlaceBinding

const val SEARCH_RESULT_PLACE_LAYOUT = R.layout.search_place

class SearchPlaceViewHolder(
    private val binding: SearchPlaceBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var place: SearchResult.PlaceItem

    internal inner class Handler {
        fun onClick() {
            searchResultClickListener(place)
        }
    }

    fun bind(place: SearchResult.PlaceItem) {
        this.place = place
        binding.place = place
        binding.handler = Handler()
    }
}