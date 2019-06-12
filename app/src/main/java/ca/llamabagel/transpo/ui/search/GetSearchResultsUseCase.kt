/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.SearchRepository
import ca.llamabagel.transpo.di.StringsGen
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combineLatest
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSearchResultsUseCase @Inject constructor(
    private val repository: SearchRepository,
    private val strings: StringsGen
) {

    @FlowPreview
    operator fun invoke(): Flow<List<SearchResult>> = repository.routeChannel
        .asFlow()
        .combineLatest(repository.stopChannel.asFlow(), repository.placeChannel.asFlow()) { routes, stops, places ->

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

            return@combineLatest searchResults
    }

}