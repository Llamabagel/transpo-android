/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.data

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.di.GeocodingWrapper
import ca.llamabagel.transpo.di.StringUtils
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

private const val SEARCH_RESULT_LIMIT = 5L
private const val SEARCH_RESULT_FILTERED_LIMIT = 20L

@Singleton
@FlowPreview
@ExperimentalCoroutinesApi
class SearchRepository @Inject constructor(
    private val database: TransitDatabase,
    private val strings: StringUtils,
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
        val filterState = if (filters.allOff) filters.copy(stops = true, routes = true, places = true) else filters

        val resultLimit = if (filters.allOff) SEARCH_RESULT_LIMIT else SEARCH_RESULT_FILTERED_LIMIT
        val exclusionList = getRecent(query, filterState, resultLimit)

        getRoutes(query.takeIf { filterState.routes }.orEmpty(), exclusionList, resultLimit)
        getStops(query.takeIf { filterState.stops }.orEmpty(), exclusionList, resultLimit)
        getPlaces(query.takeIf { filterState.places }.orEmpty(), exclusionList, resultLimit.toInt())
    }

    private fun getRecent(query: String, filters: SearchFilter, resultLimit: Long): List<String> {
        val recentList = if (query.isEmpty()) {
            database.recentSearchQueries.getMostRecent(filters.getOffFiltersList(), SEARCH_RESULT_FILTERED_LIMIT)
        } else {
            database.recentSearchQueries.searchRecentStops(filters.getOffFiltersList(), query, resultLimit)
        }.executeAsList()

        recentList.map { recent ->
            RecentResult(
                strings.bold(recent.primary_text, query),
                strings.bold(recent.secondary_text, query),
                recent.number.takeIf { it != null }?.let { strings.bold(it, query) },
                recent.code.takeIf { it != null }?.let { strings.bold(it, query) },
                recent.type,
                recent.id
            )
        }
            .let(recentChannel::offer)

        return recentList.map { it.id }
    }

    private suspend fun getStops(query: String, recent: List<String>, resultLimit: Long) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            database.stopQueries
                .getStopsByName(recent, "$query*", resultLimit)
                .executeAsList()
                .map {
                    StopResult(
                        strings.bold(it.name, query),
                        strings.bold("• ${it.code}", query),
                        strings.get(R.string.search_stop_no_trips),
                        it.id
                    )
                }
        }.orEmpty().let(stopChannel::offer)
    }

    private suspend fun getRoutes(query: String, recent: List<String>, resultLimit: Long) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            database.routeQueries
                .getRoutes(recent, "$query*", resultLimit)
                .executeAsList()
                .map {
                    RouteResult(
                        "Name",
                        strings.bold(it.short_name, query),
                        it.type.toString(),
                        it.id
                    )
                }
        }.orEmpty().let(routeChannel::offer)
    }

    private suspend fun getPlaces(query: String, recent: List<String>, resultLimit: Int) = withContext(dispatcher.io) {
        query.takeIf { it.isNotEmpty() }?.let {
            geocoder.getAutocompleteResults(query, MIN_LNG, MIN_LAT, MAX_LNG, MAX_LAT)
                .filterNot { recent.contains(it.id()) }
                .map {
                    PlaceResult(
                        strings.bold(it.text().orEmpty(), query),
                        strings.bold(it.placeName().orEmpty(), query),
                        it.id().orEmpty()
                    )
                }
                .take(resultLimit)
        }.orEmpty().let(placeChannel::offer)
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