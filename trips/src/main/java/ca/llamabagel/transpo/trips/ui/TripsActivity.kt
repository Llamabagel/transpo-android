/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.trips.R
import ca.llamabagel.transpo.trips.databinding.ActivityTripsBinding
import ca.llamabagel.transpo.trips.di.inject
import ca.llamabagel.transpo.utils.Activities
import javax.inject.Inject

class TripsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: TripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)

        val binding: ActivityTripsBinding = DataBindingUtil.setContentView(this, R.layout.activity_trips)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.loadStop(intent.getStringExtra(Activities.Trips.EXTRA_STOP_ID))

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
        })
    }
}