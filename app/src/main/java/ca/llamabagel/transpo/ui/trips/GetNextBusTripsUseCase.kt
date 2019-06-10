/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNextBusTripsUseCase @Inject constructor(private val repository: TripsRepository) {

    @FlowPreview
    @ExperimentalCoroutinesApi
    operator fun invoke(groupByDirection: Boolean = false): Flow<List<TripAdapterItem>> {

        return repository.cachedResult
            .asFlow()
            .flowOn(Dispatchers.Default)
            .map { (_, routes) ->
                routes.flatMap { route ->
                    route.trips.map { trip -> TripItem(route, trip) }
                }.sortedBy { it.trip.adjustedScheduleTime }
            }
            .flowOn(Dispatchers.Main)
    }
}