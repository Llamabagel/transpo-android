/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

sealed class SearchResult {

    data class CategoryHeader(val header: String) : SearchResult()

    data class RouteItem(val name: String, val number: String, val type: String) : SearchResult()

    data class StopItem(val name: String, val code: String, val routes: String, val id: String) : SearchResult()

    data class PlaceItem(val primary: String, val secondary: String) : SearchResult()
}