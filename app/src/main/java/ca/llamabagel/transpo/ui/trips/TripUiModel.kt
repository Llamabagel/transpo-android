/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.models.trips.Trip

data class TripUiModel(
    val routeNumber: String,
    val routeType: String,
    val destination: String,
    val adjustedScheduleTime: Int,
    val adjustmentAge: Float,
    val lastTripOfSchedule: Boolean,
    val busType: String,
    val hasBikeRack: Boolean
) {
    constructor(route: Route, trip: Trip) : this(
        route.number,
        route.number,
        trip.destination,
        trip.adjustedScheduleTime,
        trip.adjustmentAge,
        trip.lastTripOfSchedule,
        trip.busType,
        trip.hasBikeRack
    )

    val adjustedScheduleTimeString = adjustedScheduleTime.toString()
}