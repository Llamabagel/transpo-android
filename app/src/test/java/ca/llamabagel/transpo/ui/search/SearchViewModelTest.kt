/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.SearchRepository
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import ca.llamabagel.transpo.utils.stubReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchRepository: SearchRepository

    @InjectMocks
    private lateinit var searchViewModel: SearchViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun `when activity starts, keyboard state is set to open`() {
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.OPEN)
    }

    @Test
    fun `when keyboard is closed, keyboard state is set to closed`() {
        searchViewModel.notifyClosed()
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.CLOSED)
    }

    @Test
    fun `when activity starts, there are no search results`() {
        assertEquals(searchViewModel.searchResults.value, emptyList<SearchResult>())
    }

    @Test
    fun `when query is not typed in, search repository is not called`() {
        verifyZeroInteractions(searchRepository)
    }

    @Test
    fun `when search query typed in, search repository is called`() = runBlockingTest {
        searchRepository.getSearchResults("someQuery").stubReturn(emptyList<SearchResult>())
        searchViewModel.fetchSearchResults("someQuery")

        verify(searchRepository).getSearchResults("someQuery")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}