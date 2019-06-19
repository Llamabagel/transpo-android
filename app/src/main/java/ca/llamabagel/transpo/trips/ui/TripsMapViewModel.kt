/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import androidx.lifecycle.ViewModel
import ca.llamabagel.transpo.trips.domain.GetSelectedRouteTripsUseCase
import ca.llamabagel.transpo.trips.domain.UpdateTripDataUseCase
import javax.inject.Inject

class TripsMapViewModel @Inject constructor(
    private val updateTripData: UpdateTripDataUseCase,
    private val getSelectedRouteTrips: GetSelectedRouteTripsUseCase
) : ViewModel() {

    private lateinit var stopId: String
}