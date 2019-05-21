/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di.trips

import ca.llamabagel.transpo.coreComponent
import ca.llamabagel.transpo.di.TransitDatabaseModule
import ca.llamabagel.transpo.ui.trips.TripsActivity

fun inject(activity: TripsActivity) {
    DaggerTripsComponent.builder()
        .tripsActivity(activity)
        .coreComponent(activity.coreComponent())
        .transitDatabaseModule(TransitDatabaseModule(activity))
        .build()
        .inject(activity)
}