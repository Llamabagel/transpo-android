/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(private val database: TransitDatabase) {
    suspend fun getStops(query: String): List<SearchResult> = withContext(Dispatchers.IO) {
        val stops = database.stopQueries.getStopsByName("$query%").executeAsList()
        val routes = database.routeQueries.getRoutes("$query%").executeAsList()

        val searchResults = mutableListOf<SearchResult>()

        if (routes.isNotEmpty()) {
            searchResults.add(SearchResult.CategoryHeader("Routes"))
            searchResults.addAll(routes.map { SearchResult.RouteItem("Name", it.short_name, it.type.toString()) })
        }

        if (stops.isNotEmpty()) {
            searchResults.add(SearchResult.CategoryHeader("Stops"))
            searchResults.addAll(stops.map { SearchResult.StopItem(it.name, it.code, "No upcoming trips") })
        }

        searchResults
    }
}