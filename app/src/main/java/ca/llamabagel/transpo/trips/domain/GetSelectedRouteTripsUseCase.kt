/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.trips.ui.RouteSelection
import ca.llamabagel.transpo.trips.ui.adapter.TripAdapterItem
import ca.llamabagel.transpo.trips.ui.adapter.TripItem
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetSelectedRouteTripsUseCase @Inject constructor(
    private val repository: TripsRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(stopId: StopId, selectedRoutes: Array<RouteSelection>): Flow<List<TripAdapterItem>> =
        repository.getResultCache(stopId)
            .asFlow()
            .flowOn(dispatcherProvider.computation)
            .map { response -> transformApiResponse(response, selectedRoutes) }
            .flowOn(dispatcherProvider.main)

    private fun transformApiResponse(
        response: ApiResponse,
        selectedRoutes: Array<RouteSelection>
    ): List<TripAdapterItem> =
        response.routes
            .filter { route ->
                selectedRoutes.find { it.number == route.number && it.directionId == route.directionId } != null
            }
            .flatMap { route -> route.trips.map { trip -> TripItem(route, trip) } }
            .sortedBy { (_, trip) -> trip.adjustedScheduleTime }
}