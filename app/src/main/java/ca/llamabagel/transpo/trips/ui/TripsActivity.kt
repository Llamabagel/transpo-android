/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.ActivityTripsBinding
import ca.llamabagel.transpo.di.injector

const val STOP_ID_EXTRA = "stop_id"

class TripsActivity : AppCompatActivity() {

    private val args: TripsActivityArgs by navArgs()

    private val viewModel: TripsViewModel by viewModels { injector.tripsViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTripsBinding = DataBindingUtil.setContentView(this, R.layout.activity_trips)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Manually set navigation graph because of extra arguments
        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.trips, intent.extras)

        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, AppBarConfiguration(setOf()))

        viewModel.loadStop(args.stopId)

        viewModel.stop.observe(this, Observer { stop ->
            if (stop == null) {
                Toast.makeText(
                    this,
                    "Could not load stop with id ${intent.getStringExtra(STOP_ID_EXTRA)}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@Observer
            }

            viewModel.getTrips()
        })
    }
}