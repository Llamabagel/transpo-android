/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

sealed class SearchResult {

    abstract infix fun sameAs(other: SearchResult): Boolean

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResult>() {

            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
                oldItem sameAs newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    data class CategoryHeader(val header: String) : SearchResult() {
        override fun sameAs(other: SearchResult): Boolean {
            if (other !is CategoryHeader) return false

            return other.header == header
        }
    }

    data class RouteItem(val name: String, val number: String, val type: String) : SearchResult() {
        override fun sameAs(other: SearchResult): Boolean {
            if (other !is RouteItem) return false

            return other.name == name && other.number == number && other.type == type
        }
    }

    data class StopItem(val name: String, val code: String, val routes: String, val id: String) : SearchResult() {
        override fun sameAs(other: SearchResult): Boolean {
            if (other !is StopItem) return false

            return other.id == id && other.code == code && other.name == name && other.routes == routes
        }
    }

    data class PlaceItem(val primary: String, val secondary: String) : SearchResult() {
        override fun sameAs(other: SearchResult): Boolean {
            if (other !is PlaceItem) return false

            return other.primary == primary && other.secondary == secondary
        }
    }
}