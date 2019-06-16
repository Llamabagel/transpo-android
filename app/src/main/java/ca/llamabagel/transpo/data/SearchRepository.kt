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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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
    private val dispatcher: CoroutinesDispatcherProvider
) {

    private val routeChannel = ConflatedBroadcastChannel<List<SearchResult.RouteItem>>()
    val routeFlow get() = routeChannel.asFlow()

    private val stopChannel = ConflatedBroadcastChannel<List<SearchResult.StopItem>>()
    val stopFlow get() = stopChannel.asFlow()

    private val placeChannel = ConflatedBroadcastChannel<List<SearchResult.PlaceItem>>()
    val placeFlow get() = placeChannel.asFlow()

    suspend fun getSearchResults(query: String) {
        getRoutes(query)
        getStops(query)
        getPlaces(query)
    }

    private suspend fun getStops(query: String) = withContext(dispatcher.io) {
        val stops = database.takeIf { query.isNotEmpty() }
            ?.stopQueries
            ?.getStopsByName("$query*")
            ?.executeAsList()
            ?.map { SearchResult.StopItem(it.name, "• ${it.code}", strings.get(R.string.search_stop_no_trips), it.id) }
            .orEmpty()

        stopChannel.offer(stops)
    }

    private suspend fun getRoutes(query: String) = withContext(dispatcher.io) {
        val routes = database.takeIf { query.isNotEmpty() }
            ?.routeQueries
            ?.getRoutes("$query%")
            ?.executeAsList()
            ?.map { SearchResult.RouteItem("Name", it.short_name, it.type.toString()) } // TODO: update name parameter
            .orEmpty()

        routeChannel.offer(routes)
    }

    private suspend fun getPlaces(query: String) = withContext(dispatcher.io) {
        val places = MapboxGeocoding.builder()
            .accessToken(MAPBOX_KEY)
            .query(query)
            .bbox(MIN_LNG, MIN_LAT, MAX_LNG, MAX_LAT)
            .build()
            .executeCall()
            .body()
            ?.features()
            ?.map { feature -> SearchResult.PlaceItem(feature.placeName().orEmpty(), feature.text().orEmpty()) }
            .orEmpty()

        placeChannel.offer(places)
    }
}