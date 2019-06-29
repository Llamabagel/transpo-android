/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)
        val filterBtn = findViewById<ImageButton>(R.id.filter_button)
        val recycler = findViewById<RecyclerView>(R.id.search_results_list)
        val adapter = SearchAdapter(::onItemClicked)
        val popup = PopupMenu(this, filterBtn)

        popup.menuInflater.inflate(R.menu.menu_filter, popup.menu)
        recycler.adapter = adapter

        searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchBarFocusChanged(hasFocus)
        }

        searchBar.doOnTextChanged { text, _, _, _ ->
            viewModel.fetchSearchResults(text)
        }

        filterBtn.setOnClickListener {
            popup.show()
        }

        popup.setOnMenuItemClickListener { item ->
            item.isChecked = !item.isChecked
            viewModel.notifyFilterChanged(item.itemId)

            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
            item.actionView = View(this)
            item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean = false
                override fun onMenuItemActionCollapse(item: MenuItem): Boolean = false
            })
            return@setOnMenuItemClickListener false
        }

        viewModel.searchResults.observe(this, Observer(adapter::submitList))

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
        val returnIntent = Intent().apply {
            putExtra(ID_EXTRA, item.id)
            putExtra(TYPE_EXTRA, item.type)
        }

        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
