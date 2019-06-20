/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.data

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.di.GeocodingWrapper
import ca.llamabagel.transpo.di.StringsGen
import ca.llamabagel.transpo.search.data.OttawaBoundaries.MAX_LAT
import ca.llamabagel.transpo.search.data.OttawaBoundaries.MAX_LNG
import ca.llamabagel.transpo.search.data.OttawaBoundaries.MIN_LAT
import ca.llamabagel.transpo.search.data.OttawaBoundaries.MIN_LNG
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val ROUTE_RESULT_LIMIT = 5L
private const val STOP_RESULT_LIMIT = 5L
private const val PLACE_RESULT_LIMIT = 5

object OttawaBoundaries {
    const val MIN_LAT = 45.203039
    const val MIN_LNG = -76.227729
    const val MAX_LAT = 45.513359
    const val MAX_LNG = -75.351405
}

@Singleton
@FlowPreview
@ExperimentalCoroutinesApi
class SearchRepository @Inject constructor(
    private val database: TransitDatabase,
    private val strings: StringsGen,
    private val dispatcher: CoroutinesDispatcherProvider,
    private val geocoder: GeocodingWrapper
) {

    private val routeChannel = ConflatedBroadcastChannel<List<SearchResult.RouteItem>>()
    val routeFlow get() = routeChannel.asFlow()

    private val stopChannel = ConflatedBroadcastChannel<List<SearchResult.StopItem>>()
    val stopFlow get() = stopChannel.asFlow()

    private val placeChannel = ConflatedBroadcastChannel<List<SearchResult.PlaceItem>>()
    val placeFlow get() = placeChannel.asFlow()

    suspend fun getSearchResults(query: String, filters: SearchFilter) {
        getRoutes(query.takeIf { filters.routes }.orEmpty())
        getStops(query.takeIf { filters.stops }.orEmpty())
        getPlaces(query.takeIf { filters.places }.orEmpty())
    }

    private suspend fun getStops(query: String) = withContext(dispatcher.io) {
        val stops = database.takeIf { query.isNotEmpty() }
            ?.stopQueries
            ?.getStopsByName("$query*", STOP_RESULT_LIMIT)
            ?.executeAsList()
            ?.map { SearchResult.StopItem(it.name, "• ${it.code}", strings.get(R.string.search_stop_no_trips), it.id) }
            .orEmpty()

        stopChannel.offer(stops)
    }

    private suspend fun getRoutes(query: String) = withContext(dispatcher.io) {
        val routes = database.takeIf { query.isNotEmpty() }
            ?.routeQueries
            ?.getRoutes("$query%", ROUTE_RESULT_LIMIT)
            ?.executeAsList()
            ?.map { SearchResult.RouteItem("Name", it.short_name, it.type.toString()) } // TODO: update name parameter
            .orEmpty()

        routeChannel.offer(routes)
    }

    private suspend fun getPlaces(query: String) = withContext(dispatcher.io) {
        val places = geocoder.takeIf { query.isNotEmpty() }
            ?.getAutocompleteResults(query, MIN_LNG, MIN_LAT, MAX_LNG, MAX_LAT, PLACE_RESULT_LIMIT)
            ?.map { feature -> SearchResult.PlaceItem(feature.placeName().orEmpty(), feature.text().orEmpty()) }
            .orEmpty()

        placeChannel.offer(places)
    }
}