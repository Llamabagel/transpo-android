/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.ui.search.viewholders.*

class SearchAdapter(
    private val list: List<SearchResult>,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SEARCH_CATEGORY_HEADER_LAYOUT -> SearchCategoryViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_CATEGORY_HEADER_LAYOUT, parent, false)
            )
            SEARCH_RESULT_ROUTE_LAYOUT -> SearchRouteViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_RESULT_ROUTE_LAYOUT, parent, false),
                searchResultClickListener
            )
            SEARCH_RESULT_STOP_LAYOUT -> SearchStopViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_RESULT_STOP_LAYOUT, parent, false),
                searchResultClickListener
            )
            SEARCH_RESULT_PLACE_LAYOUT -> SearchPlaceViewHolder(
                DataBindingUtil.inflate(layoutInflater, SEARCH_RESULT_PLACE_LAYOUT, parent, false),
                searchResultClickListener
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
            is SearchResult.PlaceItem -> (holder as SearchPlaceViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = when (list[position]) {
        is SearchResult.CategoryHeader -> SEARCH_CATEGORY_HEADER_LAYOUT
        is SearchResult.RouteItem -> SEARCH_RESULT_ROUTE_LAYOUT
        is SearchResult.StopItem -> SEARCH_RESULT_STOP_LAYOUT
        is SearchResult.PlaceItem -> SEARCH_RESULT_PLACE_LAYOUT
    }
}