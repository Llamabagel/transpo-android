/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.home.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.search.ui.SearchActivity
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.ID_EXTRA
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.SEARCH_REQUEST_CODE
import ca.llamabagel.transpo.search.ui.SearchActivity.Companion.TYPE_EXTRA
import ca.llamabagel.transpo.trips.ui.TripsActivityDirections
import ca.llamabagel.transpo.utils.startActivityForResult
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

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home, R.id.saved, R.id.map, R.id.planner))
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)

        findViewById<EditText>(R.id.search_bar).setOnClickListener {
            startActivityForResult<SearchActivity>(this, SEARCH_REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            when (data?.getStringExtra(TYPE_EXTRA)) {
                "stop" -> data.getStringExtra(ID_EXTRA).takeIf { it != null }?.let { stopId ->
                    val navController = findNavController(R.id.nav_host_fragment)
                    val action = TripsActivityDirections.actionGlobalTripsActivity(stopId)
                    navController.navigate(action)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
