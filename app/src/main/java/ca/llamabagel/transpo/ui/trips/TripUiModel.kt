/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.models.trips.Trip

data class TripUiModel(
    val route: Route,
    val trip: Trip,
    val selected: Boolean = false
) {

    val adjustedScheduleTimeString get() = trip.adjustedScheduleTime.toString()
}