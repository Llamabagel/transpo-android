/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchRecentBinding

const val SEARCH_RESULT_RECENT_LAYOUT = R.layout.search_recent

class SearchRecentViewHolder(
    private val binding: SearchRecentBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var recent: SearchResult.RecentItem

    inner class Handler {
        fun onClick() = searchResultClickListener(recent)
    }

    fun bind(recent: SearchResult.RecentItem) {
        this.recent = recent
        binding.recent = recent
        binding.handler = Handler()
    }
}