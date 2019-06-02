/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.BuildConfig.MAPBOX_KEY
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.OttawaBoundaries.MAX_LAT
import ca.llamabagel.transpo.data.OttawaBoundaries.MAX_LNG
import ca.llamabagel.transpo.data.OttawaBoundaries.MIN_LAT
import ca.llamabagel.transpo.data.OttawaBoundaries.MIN_LNG
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.di.StringsGen
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

object OttawaBoundaries {
    const val MIN_LAT = 45.203039
    const val MIN_LNG = -76.227729
    const val MAX_LAT = 45.513359
    const val MAX_LNG = -75.351405
}

class SearchRepository @Inject constructor(private val database: TransitDatabase, private val strings: StringsGen) {

    suspend fun getSearchResults(query: String): List<SearchResult> = withContext(Dispatchers.IO) {
        if (query.isEmpty()) return@withContext emptyList<SearchResult>()

        val searchResults = mutableListOf<SearchResult>()

        getStops(query).takeIf { it.isNotEmpty() }?.let { stops ->
            searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_routes)))
            searchResults.addAll(stops)
        }

        getRoutes(query).takeIf { it.isNotEmpty() }?.let { routes ->
            searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_routes)))
            searchResults.addAll(routes)
        }

        getPlaces(query).takeIf { it.isNotEmpty() }?.let { places ->
            searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_places)))
            searchResults.addAll(places)
        }

        return@withContext searchResults
    }

    private suspend fun getStops(query: String): List<SearchResult.StopItem> = withContext(Dispatchers.IO) {
        database.stopQueries
            .getStopsByName("$query*")
            .executeAsList()
            .map { SearchResult.StopItem(it.name, "• ${it.code}", strings.get(R.string.search_stop_no_trips), it.id) }
    }

    private suspend fun getRoutes(query: String): List<SearchResult.RouteItem> = withContext(Dispatchers.IO) {
        database.routeQueries
            .getRoutes("$query%")
            .executeAsList()
            .map { SearchResult.RouteItem("Name", it.short_name, it.type.toString()) } // TODO: update name parameter
    }

    private suspend fun getPlaces(query: String): List<SearchResult.PlaceItem> = withContext(Dispatchers.IO) {
        MapboxGeocoding.builder()
            .accessToken(MAPBOX_KEY)
            .query(query)
            .bbox(MIN_LNG, MIN_LAT, MAX_LNG, MAX_LAT)
            .build()
            .executeCall()
            .body()
            ?.features()
            ?.map { feature -> SearchResult.PlaceItem(feature.placeName().orEmpty(), feature.text().orEmpty()) }
            .orEmpty()
    }
}