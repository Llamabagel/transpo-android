/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.*
import ca.llamabagel.transpo.search.data.SearchFilters.*
import ca.llamabagel.transpo.search.ui.viewholders.*
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

        assertEquals(listOf(TestStops.walkleyJasper.toSearchResult()), repository.stopFlow.first())
    }

    @Test
    fun `when there is a matching route, route search result is offered`() = runBlockingTest {
        repository.getSearchResults("44", filters)

        assertEquals(listOf(TestRoutes.route44.toSearchResult()), repository.routeFlow.first())
    }

    @Test
    fun `when there is a matching place, place search result is offered`() = runBlockingTest {
        repository.getSearchResults("Parliament", filters)

        assertEquals(listOf(TestPlace.parliament.toSearchResult()), repository.placeFlow.first())
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

    @Test
    fun `when item is clicked, it is first in recent results list`() = runBlockingTest {
        repository.pushRecent("primary", "secondary", null, null, "skdjf2034", PLACE)
        repository.getSearchResults("", filters)

        assertEquals(
            listOf(
                RecentResult("primary", "secondary", null, null, PLACE, "skdjf2034"),
                TestRecent.mackenzieKing.toSearchResult(),
                TestRecent.route95.toSearchResult(),
                TestRecent.laurier110.toSearchResult()
            ),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when recent item is clicked twice, only one item is emitted`() = runBlockingTest {
        repository.pushRecent("primary", "secondary", null, null, "skdjf2034", PLACE)
        repository.pushRecent("primary", "secondary", null, null, "skdjf2034", PLACE)
        repository.getSearchResults("", filters)

        assertEquals(
            listOf(
                RecentResult("primary", "secondary", null, null, PLACE, "skdjf2034"),
                TestRecent.mackenzieKing.toSearchResult(),
                TestRecent.route95.toSearchResult(),
                TestRecent.laurier110.toSearchResult()
            ),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when search is empty, recent item is emitted`() = runBlockingTest {
        repository.getSearchResults("", filters)

        assertEquals(
            listOf(
                TestRecent.mackenzieKing.toSearchResult(),
                TestRecent.route95.toSearchResult(),
                TestRecent.laurier110.toSearchResult()
            ),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when search is not empty and there are no matching recent results, empty list is emitted`() = runBlockingTest {
        repository.getSearchResults("skdfjnksdjfnsdkj", filters)

        assertEquals(emptyList<SearchResult>(), repository.recentFlow.first())
    }

    @Test
    fun `when search is not empty and there are matching recent results, recent item is emitted`() = runBlockingTest {
        repository.getSearchResults("mackenzie", filters)

        assertEquals(listOf(TestRecent.mackenzieKing.toSearchResult()), repository.recentFlow.first())
    }

    @Test
    fun `when stop filter is turned off recent stop results are not shown`() = runBlockingTest {
        repository.getSearchResults("mackenzie", SearchFilter(stops = false))

        assertEquals(emptyList<SearchResult>(), repository.recentFlow.first())
    }

    @Test
    fun `when route filter is turned off recent stop results are not shown`() = runBlockingTest {
        repository.getSearchResults("95", SearchFilter(routes = false))

        assertEquals(emptyList<SearchResult>(), repository.recentFlow.first())
    }

    @Test
    fun `when place filter is turned off recent stop results are not shown`() = runBlockingTest {
        repository.getSearchResults("110 Laurier", SearchFilter(places = false))

        assertEquals(emptyList<SearchResult>(), repository.recentFlow.first())
    }

    @Test
    fun `when recent stop is also a stop result, only show the recent stop`() = runBlockingTest {
        repository.pushRecent("Mackenzie King 2A", "", null, "3000", TestStops.mackenzieKing2A.id.value, STOP)
        repository.getSearchResults("Mackenzie King 2A", filters)

        assertEquals(emptyList<StopResult>(), repository.stopFlow.first())
    }

    @Test
    fun `when recent route is also a route result, only show the recent route`() = runBlockingTest {
        repository.pushRecent("", "", "44", null, TestRoutes.route44.id, ROUTE)
        repository.getSearchResults("44", filters)

        assertEquals(emptyList<RouteResult>(), repository.routeFlow.first())
    }

    @Test
    fun `when recent place is also a place result, only show the recent place`() = runBlockingTest {
        repository.pushRecent("Parliament", "", null, null, TestPlace.parliament.id()!!, PLACE)
        repository.getSearchResults("Parliament", filters)

        assertEquals(emptyList<PlaceResult>(), repository.placeFlow.first())
    }
}