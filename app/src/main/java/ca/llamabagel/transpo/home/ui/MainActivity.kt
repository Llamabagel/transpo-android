/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.home.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.search.data.SearchFilters
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.ID_EXTRA
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.SEARCH_REQUEST_CODE
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.TYPE_EXTRA
import ca.llamabagel.transpo.trips.ui.TripsActivityDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            when (data.getSerializableExtra(TYPE_EXTRA) as SearchFilters) {
                SearchFilters.STOP -> data.getStringExtra(ID_EXTRA)?.let { stopId ->
                    val navController = findNavController(R.id.nav_host_fragment)
                    val action = TripsActivityDirections.actionGlobalTripsActivity(stopId)
                    navController.navigate(action)
                }
                else -> {
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
