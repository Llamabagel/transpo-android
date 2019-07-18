/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.StringsGen
import ca.llamabagel.transpo.search.data.SearchRepository
import ca.llamabagel.transpo.search.ui.viewholders.CategoryHeader
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineLatest
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class GetSearchResultsUseCase @Inject constructor(
    private val repository: SearchRepository,
    private val strings: StringsGen
) {

    operator fun invoke(): Flow<List<SearchResult>> = repository.routeFlow
        .combineLatest(
            repository.stopFlow,
            repository.placeFlow,
            repository.recentFlow
        ) { routes, stops, places, recent ->

            val searchResults = mutableListOf<SearchResult>()

            recent.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(CategoryHeader(strings.get(R.string.search_category_recent)))
                searchResults.addAll(recent)
            }

            routes.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(CategoryHeader(strings.get(R.string.search_category_routes)))
                searchResults.addAll(routes)
            }

            stops.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(CategoryHeader(strings.get(R.string.search_category_stops)))
                searchResults.addAll(stops)
            }

            places.takeIf { it.isNotEmpty() }?.let {
                searchResults.add(CategoryHeader(strings.get(R.string.search_category_places)))
                searchResults.addAll(places)
            }

            return@combineLatest searchResults
        }
}