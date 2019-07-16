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
import ca.llamabagel.transpo.search.ui.viewholders.PlaceResult
import ca.llamabagel.transpo.search.ui.viewholders.RecentResult
import ca.llamabagel.transpo.search.ui.viewholders.RouteResult
import ca.llamabagel.transpo.search.ui.viewholders.StopResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val ROUTE_RESULT_LIMIT = 5L
private const val STOP_RESULT_LIMIT = 5L
private const val PLACE_RESULT_LIMIT = 5
private const val RECENT_EMPTY_QUERY_RESULT_LIMIT = 10L
private const val RECENT_RESULT_LIMIT = 5L

@Singleton
@FlowPreview
@ExperimentalCoroutinesApi
class SearchRepository @Inject constructor(
    private val database: TransitDatabase,
    private val strings: StringsGen,
    private val dispatcher: CoroutinesDispatcherProvider,
    private val geocoder: GeocodingWrapper
) {

    private val routeChannel = ConflatedBroadcastChannel<List<RouteResult>>()
    val routeFlow get() = routeChannel.asFlow()

    private val stopChannel = ConflatedBroadcastChannel<List<StopResult>>()
    val stopFlow get() = stopChannel.asFlow()

    private val placeChannel = ConflatedBroadcastChannel<List<PlaceResult>>()
    val placeFlow get() = placeChannel.asFlow()

    private val recentChannel = ConflatedBroadcastChannel<List<RecentResult>>()
    val recentFlow get() = recentChannel.asFlow()

    suspend fun getSearchResults(query: String, filters: SearchFilter) {
        getRoutes(query.takeIf { filters.routes }.orEmpty())
        getStops(query.takeIf { filters.stops }.orEmpty())
        getPlaces(query.takeIf { filters.places }.orEmpty())
        getRecent(query.takeIf { filters.recent }.orEmpty(), filters)
    }

    private suspend fun getStops(query: String) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            database.stopQueries
                .getStopsByName("$query*", STOP_RESULT_LIMIT)
                .executeAsList()
                .map { StopResult(it.name, "• ${it.code}", strings.get(R.string.search_stop_no_trips), it.id) }
        }.orEmpty().let(stopChannel::offer)
    }

    private suspend fun getRoutes(query: String) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            database.routeQueries
                .getRoutes("$query%", ROUTE_RESULT_LIMIT)
                .executeAsList()
                .map { RouteResult("Name", it.short_name, it.type.toString(), it.id) } // TODO: update name
        }.orEmpty().let(routeChannel::offer)
    }

    private suspend fun getPlaces(query: String) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            geocoder.getAutocompleteResults(query, MIN_LNG, MIN_LAT, MAX_LNG, MAX_LAT, PLACE_RESULT_LIMIT)
                .map { PlaceResult(it.placeName().orEmpty(), it.text().orEmpty(), it.id().orEmpty()) }
        }.orEmpty().let(placeChannel::offer)
    }

    private suspend fun getRecent(query: String, filters: SearchFilter) = withContext(dispatcher.io) {
        if (query.isEmpty()) {
            database.recentSearchQueries
                .getMostRecent(filters.getOffFiltersList(), RECENT_EMPTY_QUERY_RESULT_LIMIT)
                .executeAsList()
                .map { RecentResult(it.primary_text, it.secondary_text, it.number, it.code, it.type, it.id) }
                .let(recentChannel::offer)
        } else {
            database.recentSearchQueries
                .searchRecentStops(filters.getOffFiltersList(), query, RECENT_RESULT_LIMIT)
                .executeAsList()
                .map { RecentResult(it.primary_text, it.secondary_text, it.number, it.code, it.type, it.id) }
                .let(recentChannel::offer)
        }
    }

    suspend fun pushRecent(
        primary: String,
        secondary: String,
        number: String?,
        code: String?,
        id: String,
        type: SearchFilters
    ) = withContext(dispatcher.io) {
        val date = Calendar.getInstance().timeInMillis / 1000
        database.recentSearchQueries.insert(id, type, date, primary, secondary, number, code)
    }
}