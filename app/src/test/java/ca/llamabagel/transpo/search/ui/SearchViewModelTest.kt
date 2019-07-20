/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.*
import ca.llamabagel.transpo.search.data.SearchFilters
import ca.llamabagel.transpo.search.domain.GetSearchResultsUseCase
import ca.llamabagel.transpo.search.domain.SetRecentSearchResultUseCase
import ca.llamabagel.transpo.search.domain.UpdateQueryUseCase
import ca.llamabagel.transpo.search.ui.viewholders.CategoryHeader
import ca.llamabagel.transpo.search.ui.viewholders.Filter
import ca.llamabagel.transpo.search.ui.viewholders.RecentResult
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
            SetRecentSearchResultUseCase(fakeRepo),
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
    fun `when query is null, search results live data emits recent results`() = runBlockingTest {
        searchViewModel.fetchSearchResults(null)

        assertEquals(recentResults(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when stop filter is first turned on, other filters are turned off`() {
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.STOP.id, isOn = true))

        assertEquals(recentResults(route = false, place = false), searchViewModel.searchResults.value)
    }

    @Test
    fun `when route filter is first turned on, other filters are turned off`() {
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.ROUTE.id, isOn = true))

        assertEquals(recentResults(stop = false, place = false), searchViewModel.searchResults.value)
    }

    @Test
    fun `when place filter is first turned on, the other filters are turned off`() {
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.PLACE.id, isOn = true))

        assertEquals(recentResults(stop = false, route = false), searchViewModel.searchResults.value)
    }

    @Test
    fun `when invalid filter is set, no filter is changed`() {
        searchViewModel.notifyFilterChanged(Filter("hello world"))
        searchViewModel.fetchSearchResults("Walkley")

        assertEquals(walkleyResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when a filter other than stop is turned on, no stop results are returned`() {
        searchViewModel.fetchSearchResults("Walkley")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.ROUTE.id, isOn = true))

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when several filters are turned on and all but stop turned back off, stop results are returned`() {
        searchViewModel.fetchSearchResults("Walkley")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.ROUTE.id, isOn = true))
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.ROUTE.id, isOn = false))

        assertEquals(walkleyResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when a filter other than route is turned on, no route results are returned`() {
        searchViewModel.fetchSearchResults("44")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.STOP.id, isOn = true))

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when several filters are turned on and all but route turned back off, route results are returned`() {
        searchViewModel.fetchSearchResults("44")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.STOP.id, isOn = true))
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.STOP.id, isOn = false))

        assertEquals(route44Result, searchViewModel.searchResults.value)
    }

    @Test
    fun `when a filter other than place is turned on, no place results are returned`() {
        searchViewModel.fetchSearchResults("Parliament")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.ROUTE.id, isOn = true))

        assertEquals(emptyList<SearchResult>(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when several filters are turned on and all but place turned back off, place results are returned`() {
        searchViewModel.fetchSearchResults("Parliament")
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.PLACE.id, isOn = true))
        searchViewModel.notifyFilterChanged(Filter(SearchFilters.PLACE.id, isOn = false))

        assertEquals(parliamentResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `when activity is started, recent results are shown`() {
        searchViewModel.fetchSearchResults("")
        assertEquals(recentResults(), searchViewModel.searchResults.value)
    }

    @Test
    fun `when item is clicked, it is added to the recent search database`() {
        searchViewModel.onSearchResultClicked(TestStops.lincolnFields.toSearchResult())
        searchViewModel.fetchSearchResults("Lincoln Fields")

        assertEquals(lincolnFieldsResult, searchViewModel.searchResults.value)
    }

    @Test
    fun `matching results are shown even if recent result is shown`() {
        searchViewModel.onSearchResultClicked(TestStops.mackenzieKing.toSearchResult())
        searchViewModel.fetchSearchResults("44")

        assertEquals(route44Result, searchViewModel.searchResults.value)
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

    private fun recentResults(route: Boolean = true, stop: Boolean = true, place: Boolean = true): List<SearchResult> {
        val list = mutableListOf<SearchResult>()
        list.add(CategoryHeader(R.string.search_category_recent.toString()))
        if (stop) list.add(TestRecent.mackenzieKing.toSearchResult())
        if (route) list.add(TestRecent.route95.toSearchResult())
        if (place) list.add(TestRecent.laurier110.toSearchResult())

        return list
    }

    private val lincolnFieldsResult = listOf(
        CategoryHeader(R.string.search_category_recent.toString()),
        RecentResult(
            TestStops.lincolnFields.name,
            R.string.search_stop_no_trips.toString(),
            null,
            "• ${TestStops.lincolnFields.code.value}",
            SearchFilters.STOP,
            TestStops.lincolnFields.id.value
        )
    )
}