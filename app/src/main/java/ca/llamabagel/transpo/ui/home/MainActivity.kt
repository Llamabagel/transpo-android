/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.core.utils.Actions
import ca.llamabagel.transpo.core.utils.TAG
import ca.llamabagel.transpo.di.inject
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.checkAndApplyDataUpdates()
        }

        Log.d(TAG, "Hello World")
    }
}
