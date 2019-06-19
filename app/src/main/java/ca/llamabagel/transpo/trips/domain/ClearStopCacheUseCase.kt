/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.data.db.StopId
import javax.inject.Inject

/**
 * Use case that ensures that a cached stop result is cleared for the given owning object
 */
class ClearStopCacheUseCase @Inject constructor(private val repository: TripsRepository) {
    operator fun invoke(stopId: StopId) {
        repository.clearCacheFor(stopId)
    }
}