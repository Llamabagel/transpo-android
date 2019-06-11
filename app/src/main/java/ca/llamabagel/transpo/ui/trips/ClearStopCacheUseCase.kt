/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TripsRepository
import javax.inject.Inject

/**
 * Use case that ensures that a cached stop result is cleared for the given owning object
 */
class ClearStopCacheUseCase @Inject constructor(private val repository: TripsRepository) {
    operator fun invoke(stopCode: String) {
        repository.clearCacheFor(stopCode)
    }
}