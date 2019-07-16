/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchRouteBinding

const val SEARCH_RESULT_ROUTE_LAYOUT = R.layout.search_route

class SearchRouteViewHolder(
    private val binding: SearchRouteBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var route: RouteResult

    inner class Handler {
        fun onClick() = searchResultClickListener(route)
    }

    fun bind(route: RouteResult) {
        this.route = route
        binding.route = route
        binding.handler = Handler()
    }
}