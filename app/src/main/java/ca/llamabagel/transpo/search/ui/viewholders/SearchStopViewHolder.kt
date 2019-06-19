/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchStopBinding

const val SEARCH_RESULT_STOP_LAYOUT = R.layout.search_stop

class SearchStopViewHolder(
    private val binding: SearchStopBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var stop: SearchResult.StopItem

    inner class Handler {
        fun onClick() = searchResultClickListener(stop)
    }

    fun bind(stop: SearchResult.StopItem) {
        this.stop = stop
        binding.stop = stop
        binding.handler = Handler()
    }
}