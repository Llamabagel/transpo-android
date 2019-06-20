/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.search.data.SearchFilterState
import ca.llamabagel.transpo.search.domain.GetSearchResultsUseCase
import ca.llamabagel.transpo.search.domain.UpdateQueryUseCase
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
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
    private val getSearchResults: GetSearchResultsUseCase,
    private val updateQuery: UpdateQueryUseCase
) : ViewModel() {

    private val _keyboardState = MutableLiveData<KeyboardState>().apply { value = KeyboardState.OPEN }
    val keyboardState: LiveData<KeyboardState> = _keyboardState

    private val _searchResults = MutableLiveData<List<SearchResult>>().apply { value = emptyList() }
    val searchResults: LiveData<List<SearchResult>> = _searchResults

    private val searchFilters = SearchFilterState()

    private var searchQuery = ""

    init {
        viewModelScope.launch {
            getSearchResults().collect {
                _searchResults.postValue(it)
            }
        }
    }

    fun searchBarFocusChanged(hasFocus: Boolean) {
        if (!hasFocus) _keyboardState.value = KeyboardState.CLOSED
    }

    fun fetchSearchResults(query: CharSequence?) {
        searchQuery = query?.toString().orEmpty()

        viewModelScope.launch {
            updateQuery(searchQuery, searchFilters)
        }
    }

    fun notifyFilterChanged(@IdRes item: Int) {
        when (item) {
            R.id.routes_filter -> searchFilters.apply { routes = !routes }
            R.id.stops_filter -> searchFilters.apply { stops = !stops }
            R.id.places_filter -> searchFilters.apply { places = !places }
        }

        viewModelScope.launch {
            updateQuery(searchQuery, searchFilters)
        }
    }
}