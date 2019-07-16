/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.*
import ca.llamabagel.transpo.search.data.SearchFilter
import ca.llamabagel.transpo.search.ui.viewholders.CategoryHeader
import ca.llamabagel.transpo.utils.FakeStringsGen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class GetSearchResultsUseCaseTest {
    private val repository = provideFakeSearchRepository()
    private val getSearchResultsUseCase = GetSearchResultsUseCase(repository, FakeStringsGen())
    private val filters = SearchFilter()

    @Test
    fun `if there are stops, then stop category header is displayed`() = runBlockingTest {
        repository.getSearchResults("Walkley", filters)

        assertEquals(walkleyResult, getSearchResultsUseCase().first())
    }

    @Test
    fun `if there are routes, then route category header is displayed`() = runBlockingTest {
        repository.getSearchResults("44", filters)

        assertEquals(route44Result, getSearchResultsUseCase().first())
    }

    @Test
    fun `if there are places, then place category header is displayed`() = runBlockingTest {
        repository.getSearchResults("Parliament", filters)

        assertEquals(parliamentResult, getSearchResultsUseCase().first())
    }

    @Test
    fun `when there are multiple result types, the order is route, stop, place`() = runBlockingTest {
        repository.getSearchResults("2", filters)

        assertEquals(search2Result, getSearchResultsUseCase().first())
    }

    private val walkleyResult = listOf(
        CategoryHeader(R.string.search_category_stops.toString()),
        TestStops.walkleyJasper.toSearchResult()
    )

    private val route44Result = listOf(
        CategoryHeader(R.string.search_category_routes.toString()),
        TestRoutes.route44.toSearchResult()
    )

    private val parliamentResult = listOf(
        CategoryHeader(R.string.search_category_places.toString()),
        TestPlace.parliament.toSearchResult()
    )

    private val search2Result = listOf(
        CategoryHeader(R.string.search_category_routes.toString()),
        TestRoutes.route2.toSearchResult(),
        CategoryHeader(R.string.search_category_stops.toString()),
        TestStops.mackenzieKing2A.toSearchResult(),
        CategoryHeader(R.string.search_category_places.toString()),
        TestPlace.lisgar29.toSearchResult()
    )
}