/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.Stop
import javax.inject.Inject

class UpdateTripDataUseCase @Inject constructor(private val repository: TripsRepository) {

    private var stop: Stop? = null

    suspend operator fun invoke(stopId: String): Result<Unit> {
        if (stop == null) {
            stop = (repository.getStop(stopId) as? Result.Success)?.data
        }
        return stop?.let { stop -> repository.getTrips(stop.code) }
            ?: Result.Error(IllegalStateException("No stop code for $stopId"))
    }
}