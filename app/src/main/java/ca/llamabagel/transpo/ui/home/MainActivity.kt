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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inject(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Actions.openTripsIntent(this, "AA100"))
        }

        Log.d(TAG, "Hello World")
    }
}
