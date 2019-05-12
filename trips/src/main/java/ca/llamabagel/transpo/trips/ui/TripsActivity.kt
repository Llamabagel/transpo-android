/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.trips.R
import ca.llamabagel.transpo.trips.di.inject
import com.google.android.material.appbar.MaterialToolbar
import javax.inject.Inject

class TripsActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: TripsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        inject(this)

        viewModel.loadStop(intent.getStringExtra("stop_id"))

        viewModel.stop.observe(this, Observer { stop ->
            if (stop == null) {
                Toast.makeText(this, "Could not load stop with id ${intent.getStringExtra("stop_id")}", Toast.LENGTH_SHORT).show()
                finish()
                return@Observer
            }

            val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

            toolbar.title = stop.name
            toolbar.subtitle = stop.code
        })

        //findViewById<TextView>(R.id.textView).text = intent.getStringExtra("stop_id")
    }
}
