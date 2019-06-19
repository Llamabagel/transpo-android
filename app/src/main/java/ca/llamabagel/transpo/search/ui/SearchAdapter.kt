/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.search.ui.viewholders.*

class SearchAdapter(private val searchResultClickListener: (SearchResult) -> Unit) :
    ListAdapter<SearchResult, RecyclerView.ViewHolder>(SearchResult.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SEARCH_CATEGORY_HEADER_LAYOUT -> SearchCategoryViewHolder(
                DataBindingUtil.inflate(
                    layoutInflater,
                    SEARCH_CATEGORY_HEADER_LAYOUT,
                    parent,
                    false
                )
            )
            SEARCH_RESULT_ROUTE_LAYOUT -> SearchRouteViewHolder(
                DataBindingUtil.inflate(
                    layoutInflater,
                    SEARCH_RESULT_ROUTE_LAYOUT,
                    parent,
                    false
                ),
                searchResultClickListener
            )
            SEARCH_RESULT_STOP_LAYOUT -> SearchStopViewHolder(
                DataBindingUtil.inflate(
                    layoutInflater,
                    SEARCH_RESULT_STOP_LAYOUT,
                    parent,
                    false
                ),
                searchResultClickListener
            )
            SEARCH_RESULT_PLACE_LAYOUT -> SearchPlaceViewHolder(
                DataBindingUtil.inflate(
                    layoutInflater,
                    SEARCH_RESULT_PLACE_LAYOUT,
                    parent,
                    false
                ),
                searchResultClickListener
            )
            else -> throw IllegalArgumentException("No ViewHolder for type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchCategoryViewHolder -> holder.bind(getItem(position) as SearchResult.CategoryHeader)
            is SearchRouteViewHolder -> holder.bind(getItem(position) as SearchResult.RouteItem)
            is SearchStopViewHolder -> holder.bind(getItem(position) as SearchResult.StopItem)
            is SearchPlaceViewHolder -> holder.bind(getItem(position) as SearchResult.PlaceItem)
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is SearchResult.CategoryHeader -> SEARCH_CATEGORY_HEADER_LAYOUT
        is SearchResult.RouteItem -> SEARCH_RESULT_ROUTE_LAYOUT
        is SearchResult.StopItem -> SEARCH_RESULT_STOP_LAYOUT
        is SearchResult.PlaceItem -> SEARCH_RESULT_PLACE_LAYOUT
    }
}