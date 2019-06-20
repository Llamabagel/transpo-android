/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.TestPlace
import ca.llamabagel.transpo.data.TestRoutes
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeSearchRepository
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository = provideFakeSearchRepository()
    private val filters = SearchFilter()

    @Test
    fun `when empty query is searched, empty list is returned in stops list`() = runBlockingTest {
        repository.getSearchResults("", filters)

        assertEquals(emptyList<SearchResult>(), repository.stopFlow.first())
    }

    @Test
    fun `when empty query is searched, empty list is returned in routes list`() = runBlockingTest {
        repository.getSearchResults("", filters)

        assertEquals(emptyList<SearchResult>(), repository.routeFlow.first())
    }

    @Test
    fun `when empty query is searched, empty list is returned in places list`() = runBlockingTest {
        repository.getSearchResults("", filters)

        assertEquals(emptyList<SearchResult>(), repository.placeFlow.first())
    }

    @Test
    fun `when the query is not empty, but there are no stop results, empty list is returned`() = runBlockingTest {
        repository.getSearchResults("ldkfjgdlfkjgd", filters)

        assertEquals(emptyList<SearchResult>(), repository.stopFlow.first())
    }

    @Test
    fun `when the query is not empty, but there are no route results, empty list is returned`() = runBlockingTest {
        repository.getSearchResults("ldkfjgdlfkjgd", filters)

        assertEquals(emptyList<SearchResult>(), repository.routeFlow.first())
    }

    @Test
    fun `when the query is not empty, but there are no place results, empty list is returned`() = runBlockingTest {
        repository.getSearchResults("ldkfjgdlfkjgd", filters)

        assertEquals(emptyList<SearchResult>(), repository.placeFlow.first())
    }

    @Test
    fun `when there is a matching stop, stop search result is offered`() = runBlockingTest {
        repository.getSearchResults("Walkley", filters)

        assertEquals(walkleyResult, repository.stopFlow.first())
    }

    @Test
    fun `when there is a matching route, route search result is offered`() = runBlockingTest {
        repository.getSearchResults("44", filters)

        assertEquals(route44Result, repository.routeFlow.first())
    }

    @Test
    fun `when there is a matching place, place search result is offered`() = runBlockingTest {
        repository.getSearchResults("Parliament", filters)

        assertEquals(parliamentResult, repository.placeFlow.first())
    }

    @Test
    fun `when stop filter is not set, empty stop list is offered`() = runBlockingTest {
        repository.getSearchResults("walkley", SearchFilter(stops = false))

        assertEquals(emptyList<SearchResult>(), repository.stopFlow.first())
    }

    @Test
    fun `when route filter is not set, empty route list is offered`() = runBlockingTest {
        repository.getSearchResults("44", SearchFilter(routes = false))

        assertEquals(emptyList<SearchResult>(), repository.routeFlow.first())
    }

    @Test
    fun `when place filter is not set, empty place list is offered`() = runBlockingTest {
        repository.getSearchResults("Parliament", SearchFilter(places = false))

        assertEquals(emptyList<SearchResult>(), repository.placeFlow.first())
    }

    private val walkleyResult = listOf(
        SearchResult.StopItem(
            TestStops.walkleyJasper.name,
            "• ${TestStops.walkleyJasper.code.value}",
            R.string.search_stop_no_trips.toString(),
            TestStops.walkleyJasper.id.value
        )
    )

    private val route44Result = listOf(
        SearchResult.RouteItem(
            "Name", // TODO: Update name parameter
            TestRoutes.route44.short_name,
            TestRoutes.route44.type.toString()
        )
    )

    private val parliamentResult = listOf(
        SearchResult.PlaceItem(TestPlace.parliament.placeName()!!, TestPlace.parliament.text()!!)
    )
}