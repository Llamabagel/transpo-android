/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchStopBinding

const val SEARCH_RESULT_STOP_LAYOUT = R.layout.search_stop

class SearchStopViewHolder(private val binding: SearchStopBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(stop: SearchResult.StopItem) {
        binding.stop = stop
    }
}