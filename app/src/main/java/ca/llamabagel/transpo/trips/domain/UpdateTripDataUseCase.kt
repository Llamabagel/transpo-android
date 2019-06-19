/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.data.db.StopId
import javax.inject.Inject

class UpdateTripDataUseCase @Inject constructor(private val repository: TripsRepository) {
    suspend operator fun invoke(stopId: StopId): Result<Unit> = repository.getTrips(stopId)
}