/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import ca.llamabagel.transpo.trips.ui.STOP_ID_EXTRA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

private const val KEYBOARD_DELAY_TIME = 200L

@FlowPreview
@ExperimentalCoroutinesApi
class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_REQUEST_CODE = 1
    }

    private val viewModel: SearchViewModel by viewModels { injector.searchViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)
        val adapter = SearchAdapter(::onItemClicked)

        findViewById<RecyclerView>(R.id.search_results_list).adapter = adapter

        viewModel.keyboardState.observe(this, Observer {
            if (it == KeyboardState.OPEN) {
                searchBar.requestFocus()
                searchBar.postDelayed({
                    val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.showSoftInput(searchBar.findFocus(), 0)
                }, KEYBOARD_DELAY_TIME)
            }
        })

        searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchBarFocusChanged(hasFocus)
        }

        searchBar.doOnTextChanged { text, _, _, _ ->
            viewModel.fetchSearchResults(text)
        }

        viewModel.searchResults.observe(this, Observer(adapter::submitList))
    }

    private fun onItemClicked(item: SearchResult) {
        when (item) {
            is SearchResult.StopItem -> {
                val returnIntent = Intent().apply { putExtra(STOP_ID_EXTRA, item.id) }
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            is SearchResult.RouteItem -> {
            }
            is SearchResult.PlaceItem -> {
            }
        }
    }
}
