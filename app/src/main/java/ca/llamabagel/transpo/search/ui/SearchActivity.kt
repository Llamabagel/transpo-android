/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.search.data.SearchFilters
import ca.llamabagel.transpo.search.ui.viewholders.Filter
import ca.llamabagel.transpo.search.ui.viewholders.SearchFilterView
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

private const val KEYBOARD_DELAY_TIME = 200L

@FlowPreview
@ExperimentalCoroutinesApi
class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_REQUEST_CODE = 1
        const val ID_EXTRA = "id_extra"
        const val TYPE_EXTRA = "type_extra"
    }

    private val viewModel: SearchViewModel by viewModels { injector.searchViewModelFactory() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel.fetchSearchResults("")

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)
        val clearSearch = findViewById<ImageButton>(R.id.clear_search_button)
        val recycler = findViewById<RecyclerView>(R.id.search_results_list)
        val adapter = SearchAdapter(::onItemClicked)
        val chipView = findViewById<SearchFilterView>(R.id.search_filter_view)
        val backButton = findViewById<ImageButton>(R.id.search_back_button)

        backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        chipView.setOnClickListener(viewModel::notifyFilterChanged)

        chipView.addFilter(
            Filter(SearchFilters.ROUTE.id, getString(R.string.search_category_routes)),
            Filter(SearchFilters.STOP.id, getString(R.string.search_category_stops)),
            Filter(SearchFilters.PLACE.id, getString(R.string.search_category_places))
        )

        recycler.adapter = adapter

        recycler.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                setResult(Activity.RESULT_CANCELED)
                finish()
                true
            } else false
        }

        searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchBarFocusChanged(hasFocus)
        }

        searchBar.doOnTextChanged { text, _, _, _ ->
            viewModel.fetchSearchResults(text)
            clearSearch.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        clearSearch.setOnClickListener { searchBar.setText("") }

        viewModel.searchResults.observe(this, Observer {
            adapter.submitList(it)
            recycler.smoothScrollToPosition(0)
        })

        viewModel.keyboardState.observe(this, Observer {
            if (it == KeyboardState.OPEN) {
                searchBar.requestFocus()
                searchBar.postDelayed({
                    val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.showSoftInput(searchBar.findFocus(), 0)
                }, KEYBOARD_DELAY_TIME)
            }
        })
    }

    private fun onItemClicked(item: SearchResult) {
        viewModel.onSearchResultClicked(item)

        val returnIntent = Intent().apply {
            putExtra(ID_EXTRA, item.id)
            putExtra(TYPE_EXTRA, item.type)
        }

        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
