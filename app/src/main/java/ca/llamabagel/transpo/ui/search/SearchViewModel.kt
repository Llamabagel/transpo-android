/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.SearchRepository
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class KeyboardState {
    OPEN, CLOSED
}

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    searchUseCase: GetSearchResultsUseCase
) : ViewModel() {

    init {
        val searchResultObserver = searchUseCase()

        viewModelScope.launch {
            searchResultObserver.collect {
                Log.d("Collector!", "Collected: $it")
                _searchResults.postValue(it)
            }
        }
    }



    private val _keyboardState = MutableLiveData<KeyboardState>().apply { value = KeyboardState.OPEN }
    val keyboardState: LiveData<KeyboardState> = _keyboardState

    private val _searchResults = MutableLiveData<List<SearchResult>>().apply { value = emptyList() }
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    fun notifyClosed() {
        _keyboardState.value = KeyboardState.CLOSED
    }

    fun fetchSearchResults(query: String) = viewModelScope.launch {
        searchRepository.getSearchResults(query)
    }
}
