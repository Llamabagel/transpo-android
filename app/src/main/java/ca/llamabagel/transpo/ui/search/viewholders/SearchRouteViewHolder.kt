/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchRouteBinding

const val SEARCH_RESULT_ROUTE_LAYOUT = R.layout.search_route

class SearchRouteViewHolder(private val binding: SearchRouteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(route: SearchResult.RouteItem) {
        binding.route = route
    }
}