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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

object OttawaBoundaries {
    const val MIN_LAT = 45.203039
    const val MIN_LNG = -76.227729
    const val MAX_LAT = 45.513359
    const val MAX_LNG = -75.351405
}

@ExperimentalCoroutinesApi
class SearchRepository @Inject constructor(private val database: TransitDatabase, private val strings: StringsGen) {

    val searchResult = ConflatedBroadcastChannel<List<SearchResult>>()

    fun getSearchResults(query: String) {

        getRoutes(query).combineLatest(getStops(query), getPlaces(query)) { routes, stops, places ->
            val searchResults = mutableListOf<SearchResult>()

            routes.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_routes)))
                searchResults.addAll(routes)
            }

            stops.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_stops)))
                searchResults.addAll(stops)
            }

            places.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(SearchResult.CategoryHeader(strings.get(R.string.search_category_places)))
                searchResults.addAll(places)
            }

            searchResult.offer(searchResults)
        }
    }

    private fun getStops(query: String): Flow<List<SearchResult.StopItem>> = flow {
        database.stopQueries
            .getStopsByName("$query*")
            .executeAsList()
            .map { SearchResult.StopItem(it.name, "• ${it.code}", strings.get(R.string.search_stop_no_trips), it.id) }
    }

    private fun getRoutes(query: String): Flow<List<SearchResult.RouteItem>> = flow {
        database.routeQueries
            .getRoutes("$query%")
            .executeAsList()
            .map { SearchResult.RouteItem("Name", it.short_name, it.type.toString()) } // TODO: update name parameter
    }

    private fun getPlaces(query: String): Flow<List<SearchResult.PlaceItem>> = flow {
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