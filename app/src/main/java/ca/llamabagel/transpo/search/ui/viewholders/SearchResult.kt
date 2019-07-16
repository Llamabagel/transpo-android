/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ca.llamabagel.transpo.search.data.SearchFilters

sealed class SearchResult {

    abstract infix fun sameAs(other: SearchResult): Boolean
    abstract val type: SearchFilters
    abstract val id: String

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResult>() {

            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean = oldItem sameAs newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) = oldItem == newItem
        }
    }
}

data class CategoryHeader(
    val header: String,
    override val id: String = "",
    override val type: SearchFilters = SearchFilters.CATEGORY
) : SearchResult() {

    override fun sameAs(other: SearchResult): Boolean {
        if (other !is CategoryHeader) return false

        return other.header == header && other.id == id
    }
}

data class RouteResult(
    val name: String,
    val number: String,
    val routeType: String,
    override val id: String
) : SearchResult() {

    override val type: SearchFilters = SearchFilters.ROUTE

    override fun sameAs(other: SearchResult): Boolean {
        if (other !is RouteResult) return false

        return other.name == name && other.number == number && other.routeType == routeType
    }
}

data class StopResult(
    val name: String,
    val code: String,
    val routes: String,
    override val id: String
) : SearchResult() {

    override val type: SearchFilters = SearchFilters.STOP

    override fun sameAs(other: SearchResult): Boolean {
        if (other !is StopResult) return false

        return other.id == id && other.code == code && other.name == name && other.routes == routes
    }
}

data class PlaceResult(
    val primary: String,
    val secondary: String,
    override val id: String
) : SearchResult() {

    override val type: SearchFilters = SearchFilters.PLACE

    override fun sameAs(other: SearchResult): Boolean {
        if (other !is PlaceResult) return false

        return other.primary == primary && other.secondary == secondary && other.id == id
    }
}

data class RecentResult(
    val primary: String,
    val secondary: String,
    val number: String?,
    val code: String?,
    override val type: SearchFilters,
    override val id: String
) : SearchResult() {

    override fun sameAs(other: SearchResult): Boolean {
        if (other !is RecentResult) return false

        return other.primary == primary && other.id == id && other.type == type
    }
}