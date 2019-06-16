/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.provideFakeSearchRepository
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
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
        searchViewModel = SearchViewModel(
            GetSearchResultsUseCase(provideFakeSearchRepository(), FakeStringsGen()),
            UpdateQueryUseCase(provideFakeSearchRepository())
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

//    @Test //TODO: figure out why this fails
//    fun `when search query typed in, search repository is called`() = runBlockingTest {
//        searchViewModel.fetchSearchResults("MAC")
//
//        assert(searchViewModel.searchResults.value?.isNotEmpty() == true)
//    }

    @Test
    fun `when query is null, empty string is searched from repository`() = runBlockingTest {
        searchViewModel.fetchSearchResults(null)

        assert(searchViewModel.searchResults.value?.isEmpty() == true)
    }
}