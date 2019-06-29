/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.TestPlace
import ca.llamabagel.transpo.data.TestRoutes
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeSearchRepository
import ca.llamabagel.transpo.search.data.SearchFilter
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
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
        SearchResult.CategoryHeader(R.string.search_category_stops.toString()),
        SearchResult.StopItem(
            TestStops.walkleyJasper.name,
            "• ${TestStops.walkleyJasper.code.value}",
            R.string.search_stop_no_trips.toString(),
            TestStops.walkleyJasper.id.value
        )
    )

    private val route44Result = listOf(
        SearchResult.CategoryHeader(R.string.search_category_routes.toString()),
        SearchResult.RouteItem(
            "Name", // TODO: Update name parameter
            TestRoutes.route44.short_name,
            TestRoutes.route44.type.toString(),
            TestRoutes.route44.id
        )
    )

    private val parliamentResult = listOf(
        SearchResult.CategoryHeader(R.string.search_category_places.toString()),
        SearchResult.PlaceItem(
            TestPlace.parliament.placeName()!!,
            TestPlace.parliament.text()!!,
            TestPlace.parliament.id()!!
        )
    )

    private val search2Result = listOf(
        SearchResult.CategoryHeader(R.string.search_category_routes.toString()),
        SearchResult.RouteItem(
            "Name", // TODO: Update name parameter
            TestRoutes.route2.short_name,
            TestRoutes.route2.type.toString(),
            TestRoutes.route2.id
        ),
        SearchResult.CategoryHeader(R.string.search_category_stops.toString()),
        SearchResult.StopItem(
            TestStops.mackenzieKing2A.name,
            "• ${TestStops.mackenzieKing2A.code.value}",
            R.string.search_stop_no_trips.toString(),
            TestStops.mackenzieKing2A.id.value
        ),
        SearchResult.CategoryHeader(R.string.search_category_places.toString()),
        SearchResult.PlaceItem(TestPlace.lisgar29.placeName()!!, TestPlace.lisgar29.text()!!, TestPlace.lisgar29.id()!!)
    )
}