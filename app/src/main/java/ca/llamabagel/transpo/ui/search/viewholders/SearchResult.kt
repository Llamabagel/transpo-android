/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

enum class SearchResultType(val id: Int) {
    CATEGORY(0), ROUTE(1), STOP(2), PLACE(3)
}

sealed class SearchResult {

    abstract val id: Int

    data class CategoryHeader(
        val header: String,
        override val id: Int = SearchResultType.CATEGORY.id
    ) : SearchResult()

    data class RouteItem(
        val name: String,
        val number: String,
        val type: String,
        override val id: Int = SearchResultType.ROUTE.id
    ) : SearchResult()

    data class StopItem(
        val name: String,
        val code: String,
        val routes: String,
        override val id: Int = SearchResultType.STOP.id
    ) : SearchResult()

    data class PlaceItem(
        val primary: String,
        val secondary: String,
        override val id: Int = SearchResultType.PLACE.id
    ) : SearchResult()
}