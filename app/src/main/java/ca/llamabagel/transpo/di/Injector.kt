/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import ca.llamabagel.transpo.TranspoApplication
import ca.llamabagel.transpo.coreComponent
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.ui.home.MainActivity

fun inject(activity: MainActivity) {
    DaggerDataComponent.builder()
        .coreComponent(activity.coreComponent())
        .sharedPreferencesModule(
            SharedPreferencesModule(activity, LocalMetadataSource.METADATA_PREF)
        )
        .transitDatabaseModule(
            TransitDatabaseModule(activity)
        )
        .mainActivity(activity)
        .build()
        .inject(activity)
}

fun inject(application: TranspoApplication) {
    DaggerWorkerComponent.builder()
        .coreComponent(application.coreComponent)
        .sharedPreferencesModule(
            SharedPreferencesModule(application, LocalMetadataSource.METADATA_PREF)
        )
        .transitDatabaseModule(
            TransitDatabaseModule(application)
        )
        .build()
        .inject(application)
}