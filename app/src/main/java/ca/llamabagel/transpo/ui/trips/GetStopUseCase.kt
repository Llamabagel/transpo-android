/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import javax.inject.Inject

/**
 * Use case that gets info about a stop.
 * TODO: Get more info about the stop
 */
class GetStopUseCase @Inject constructor(private val repository: TripsRepository) {

    suspend operator fun invoke(id: StopId): Result<Stop>  = repository.getStop(id)
}