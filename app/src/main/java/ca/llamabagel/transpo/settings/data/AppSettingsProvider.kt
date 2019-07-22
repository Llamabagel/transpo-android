package ca.llamabagel.transpo.settings.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named

class AppSettingsProvider @Inject constructor(
    @Named(SETTINGS_PREF) private val sharedPreferences: SharedPreferences
) :
    Settings {

    private val _useCellularForDataDownloads =
        Setting({ sharedPreferences.getBoolean(CELLULAR_KEY, false) },
            { sharedPreferences.edit().putBoolean(CELLULAR_KEY, it).apply() })

    override val useCellularForDataDownloads: Setting<Boolean> get() = _useCellularForDataDownloads

    private val _groupByRoute = Setting({ sharedPreferences.getBoolean(GROUP_ROUTE_KEY, false) }, {
        sharedPreferences.edit().putBoolean(
            GROUP_ROUTE_KEY, it
        ).apply()
    })

    override val groupByRoute: Setting<Boolean> get() = _groupByRoute

    companion object {
        const val SETTINGS_PREF = "settings"

        private const val CELLULAR_KEY = "use_cellular_for_data"
        private const val GROUP_ROUTE_KEY = "group_by_route"
    }
}