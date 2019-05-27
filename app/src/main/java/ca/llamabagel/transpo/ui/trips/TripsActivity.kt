/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.ActivityTripsBinding
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.utils.Activities

class TripsActivity : AppCompatActivity() {

    private val args: TripsActivityArgs by navArgs()

    private val viewModel: TripsViewModel by viewModels{ injector.tripsViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTripsBinding = DataBindingUtil.setContentView(this, R.layout.activity_trips)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Manually set navigation graph because of extra arguments
        findNavController(R.id.navHostFragment)
            .setGraph(R.navigation.trips, intent.extras)

        viewModel.loadStop(args.stopId)

        viewModel.stop.observe(this, Observer { stop ->
            if (stop == null) {
                Toast.makeText(
                    this,
                    "Could not load stop with id ${intent.getStringExtra("stop_id")}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@Observer
            }

            viewModel.getTrips()
        })
    }
}