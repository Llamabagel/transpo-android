/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import javax.inject.Inject


class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, injector.searchViewModelFactory())[SearchViewModel::class.java]

        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar1))

        val searchBar = findViewById<CustomSearchView>(R.id.search_bar)

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
    }
}
