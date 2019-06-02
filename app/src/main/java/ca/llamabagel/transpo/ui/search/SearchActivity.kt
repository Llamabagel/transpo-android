/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import ca.llamabagel.transpo.ui.trips.STOP_ID_EXTRA
import ca.llamabagel.transpo.ui.trips.TripsActivity
import ca.llamabagel.transpo.utils.startActivity

private const val KEYBOARD_DELAY_TIME = 200L

class SearchActivity : AppCompatActivity(), SearchResultClickListener {

    private val viewModel: SearchViewModel by viewModels { injector.searchViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)
        val recycler = findViewById<RecyclerView>(R.id.search_results_list)

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
            if (!hasFocus) {
                viewModel.notifyClosed()
            }
        }

        searchBar.doOnTextChanged { text, _, _, _ ->
            viewModel.fetchSearchResults(text.toString())
        }

        viewModel.searchResults.observe(this, Observer {
            recycler.adapter = SearchAdapter(it, this)
        })
    }

    override fun onItemClicked(item: SearchResult) {
        when (item) {
            is SearchResult.StopItem -> {
                startActivity<TripsActivity>(this) {
                    putExtra(STOP_ID_EXTRA, item.id)
                }
            }
            is SearchResult.RouteItem -> {
            }
            is SearchResult.PlaceItem -> {
            }
        }
    }
}
