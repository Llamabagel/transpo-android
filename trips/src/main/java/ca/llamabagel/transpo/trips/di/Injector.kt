/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.di

import ca.llamabagel.transpo.coreComponent
import ca.llamabagel.transpo.di.DaggerTripsComponent
import ca.llamabagel.transpo.di.TransitDatabaseModule
import ca.llamabagel.transpo.trips.ui.TripsActivity

fun inject(activity: TripsActivity) {
    DaggerTripsComponent.builder()
        .coreComponent(activity.coreComponent())
        .transitDatabaseModule(TransitDatabaseModule(activity))
        .build()
        .inject(activity)
}