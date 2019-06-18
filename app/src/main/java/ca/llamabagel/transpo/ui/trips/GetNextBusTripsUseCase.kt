/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNextBusTripsUseCase @Inject constructor(private val repository: TripsRepository,
                                                 private val dispatcherProvider: CoroutinesDispatcherProvider) {
    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(stopId: StopId, groupByDirection: Boolean = false): Flow<List<TripAdapterItem>> =
        repository.getResultCache(stopId)
            .asFlow()
            .map { response -> transformResponse(response) }
            .flowOn(dispatcherProvider.computation)

    private fun transformResponse(response: ApiResponse): List<TripAdapterItem> =
            response.routes
                .flatMap { route ->
                    route.trips.map { trip -> TripItem(route, trip) }
                }
                .sortedBy { (_, trip) -> trip.adjustedScheduleTime }
}