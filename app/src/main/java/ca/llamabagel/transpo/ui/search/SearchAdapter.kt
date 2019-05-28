/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.ui.search.viewholders.*

class SearchAdapter(private val list: List<SearchResult>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SearchResultType.CATEGORY.id -> SearchCategoryViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_CATEGORY_HEADER_LAYOUT, parent, false)
            )
            SearchResultType.ROUTE.id -> SearchRouteViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_RESULT_ROUTE_LAYOUT, parent, false)
            )
            SearchResultType.STOP.id -> SearchStopViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_RESULT_STOP_LAYOUT, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown search result type")
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is SearchResult.CategoryHeader -> (holder as SearchCategoryViewHolder).bind(item)
            is SearchResult.RouteItem -> (holder as SearchRouteViewHolder).bind(item)
            is SearchResult.StopItem -> (holder as SearchStopViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = list[position].id
}