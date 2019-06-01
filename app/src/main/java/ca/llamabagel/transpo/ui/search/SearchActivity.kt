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

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels { injector.searchViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar1))

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)
        val recycler = findViewById<RecyclerView>(R.id.search_results_list)

        viewModel.keyboardState.observe(this, Observer {
            if (it == KeyboardState.OPEN) {
                searchBar.requestFocus()
                searchBar.postDelayed({
                    val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.showSoftInput(searchBar.findFocus(), 0)
                }, 200)
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
            recycler.adapter = SearchAdapter(it)
        })
    }
}
