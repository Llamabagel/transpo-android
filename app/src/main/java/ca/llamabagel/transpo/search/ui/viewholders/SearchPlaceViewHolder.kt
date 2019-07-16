/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchPlaceBinding

const val SEARCH_RESULT_PLACE_LAYOUT = R.layout.search_place

class SearchPlaceViewHolder(
    private val binding: SearchPlaceBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var place: PlaceResult

    inner class Handler {
        fun onClick() = searchResultClickListener(place)
    }

    fun bind(place: PlaceResult) {
        this.place = place
        binding.place = place
        binding.handler = Handler()
    }
}