/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.inject
import ca.llamabagel.transpo.utils.Activities
import ca.llamabagel.transpo.utils.intentTo
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.checkAndApplyDataUpdates()
        }

        viewModel.workInfo.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                return@Observer
            }

            val info = it[0]

            val finished = info.state.isFinished
            findViewById<ProgressBar>(R.id.progressBar).visibility = if (finished) View.INVISIBLE else View.VISIBLE
        })

        findViewById<Button>(R.id.openButton).setOnClickListener {
            val id = findViewById<EditText>(R.id.editText).text.toString()
            startActivity(intentTo(Activities.Trips).apply { putExtra(
                Activities.Trips.EXTRA_STOP_ID, id) })
        }
    }
}
