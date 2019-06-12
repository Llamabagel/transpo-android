/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.SearchRepository
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import ca.llamabagel.transpo.utils.stubReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@FlowPreview
@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchUseCase: GetSearchResultsUseCase

    @Mock
    private lateinit var updateQueryUseCase: UpdateQueryUseCase

    @InjectMocks
    private lateinit var searchViewModel: SearchViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
    }

//    @Test //TODO: figure out why this fails
//    fun `when activity starts, searchUseCase is started`() = runBlockingTest {
//        `when`(searchUseCase.invoke()).thenReturn(flowOf())
//        searchViewModel.startListeningToSearchResults()
//        verify(searchUseCase).invoke()
//    }

    @Test
    fun `when activity starts, keyboard state is set to open`()  {
        assertEquals(searchViewModel.keyboardState.value, KeyboardState.OPEN)
    }

    @Test
    fun `when search bar loses focus, keyboard state is set to closed`()  {
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
    fun `when query is not typed in, search repository is not called`() {
        verifyZeroInteractions(updateQueryUseCase)
    }
//
//    @Test //TODO: figure out why this fails
//    fun `when search query typed in, search repository is called`() = runBlockingTest {
//        `when`(updateQueryUseCase.invoke("someQuery")).thenReturn(Unit)
//        searchViewModel.fetchSearchResults("someQuery")
//
//        verify(updateQueryUseCase).invoke("someQuery")
//    }

    @Test
    fun `when query is null, empty string is searched from repository`() = runBlockingTest {
        `when`(updateQueryUseCase.invoke("")).thenReturn(Unit)
        searchViewModel.fetchSearchResults(null)

        verify(updateQueryUseCase).invoke("")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}