/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.TestPlace
import ca.llamabagel.transpo.data.TestRoutes
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeSearchRepository
import ca.llamabagel.transpo.search.domain.GetSearchResultsUseCase
import ca.llamabagel.transpo.search.domain.UpdateQueryUseCase
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import ca.llamabagel.transpo.utils.FakeStringsGen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@FlowPreview
@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        val fakeRepo = provideFakeSearchRepository()
        searchViewModel = SearchViewModel(
            GetSearchResultsUseCase(fakeRepo, FakeStringsGen()),
            UpdateQueryUseCase(fakeRepo)
        )
    }

    @Test
    fun `when activity starts, keyboard state is set to open`() {
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.OPEN)
    }

    @Test
    fun `when search bar loses focus, keyboard state is set to closed`() {
        searchViewModel.searchBarFocusChanged(false)
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.CLOSED)
    }

    @Test
    fun `when search bar gains focus, keyboard state is not set to closed`() {
        searchViewModel.searchBarFocusChanged(true)
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.OPEN)
    }

    @Test
    fun `when activity starts, there are no search results`() {
        assertEquals(searchViewModel.searchResults.value, emptyList<SearchResult>())
    }

    @Test
    fun `when search query typed in, search result live data is updated`() = runBlockingTest {
        searchViewModel.fetchSearchResults("Walkley")

        assertEquals(walkleyResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when query is null, search results live data emits empty list`() = runBlockingTest {
        searchViewModel.fetchSearchResults(null)

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when stop filter menu item is clicked, filter is turned off`() {
        searchViewModel.notifyFilterChanged(R.id.stops_filter)
        searchViewModel.fetchSearchResults("Walkley")

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when route filter menu item is clicked, filter is turned off`() {
        searchViewModel.notifyFilterChanged(R.id.routes_filter)
        searchViewModel.fetchSearchResults("44")

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when place filter menu item is clicked, filter is turned off`() {
        searchViewModel.notifyFilterChanged(R.id.places_filter)
        searchViewModel.fetchSearchResults("Parliament")

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when filter is changed, search results are updated`() {
        searchViewModel.fetchSearchResults("Walkley")
        searchViewModel.notifyFilterChanged(R.id.stops_filter)

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when invalid filter is set, no filter is changed`() {
        searchViewModel.notifyFilterChanged(R.id.filter_button)
        searchViewModel.fetchSearchResults("Walkley")

        assertEquals(walkleyResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when stop filter is turned off and turned back on, stop results are returned`() {
        searchViewModel.notifyFilterChanged(R.id.stops_filter)
        searchViewModel.fetchSearchResults("Walkley")
        searchViewModel.notifyFilterChanged(R.id.stops_filter)

        assertEquals(walkleyResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when route filter is turned off and turned back on, route results are returned`() {
        searchViewModel.notifyFilterChanged(R.id.routes_filter)
        searchViewModel.fetchSearchResults("44")
        searchViewModel.notifyFilterChanged(R.id.routes_filter)

        assertEquals(route44Result, searchViewModel.searchResults.value)
    }

    @Test
    fun `when place filter is turned off and turned back on, place results are returned`() {
        searchViewModel.notifyFilterChanged(R.id.places_filter)
        searchViewModel.fetchSearchResults("Parliament")
        searchViewModel.notifyFilterChanged(R.id.places_filter)

        assertEquals(parliamentResult, searchViewModel.searchResults.value)
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
            TestRoutes.route44.type.toString()
        )
    )

    private val parliamentResult = listOf(
        SearchResult.CategoryHeader(R.string.search_category_places.toString()),
        SearchResult.PlaceItem(TestPlace.parliament.placeName()!!, TestPlace.parliament.text()!!)
    )
}