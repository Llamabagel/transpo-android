/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TripsMapViewModel @Inject constructor(
    private val updateTripData: UpdateTripDataUseCase,
    private val getSelectedRouteTrips: GetSelectedRouteTripsUseCase
) : ViewModel() {

    private lateinit var stopId: String


}