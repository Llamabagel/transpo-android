package ca.llamabagel.transpo.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import io.github.dellisd.quicksave.views.SwitchSetting

class SettingsActivity : AppCompatActivity() {

    private val settings by lazy { injector.appSettings() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<SwitchSetting>(R.id.group_by_route).bindSetting(settings.groupByRoute)
    }
}
