package ca.llamabagel.transpo.settings.data

import io.github.dellisd.quicksave.SettingsProvider
import io.github.dellisd.quicksave.booleanSetting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(provider: SettingsProvider) : SettingsProvider by provider {
    val groupByRoute by booleanSetting {
        title = "Group results by Route"
        caption = "Group trips by their Route number and destination"
    }

    companion object {
        const val SETTINGS_PREF = "settings"
    }
}