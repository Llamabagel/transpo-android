/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import ca.llamabagel.transpo.coreComponent
import ca.llamabagel.transpo.ui.TripsActivity

fun inject(activity: TripsActivity) {
    DaggerTripsComponent.builder()
        .coreComponent(activity.coreComponent())
        .transitDatabaseModule(TransitDatabaseModule(activity))
        .build()
        .inject(activity)
}