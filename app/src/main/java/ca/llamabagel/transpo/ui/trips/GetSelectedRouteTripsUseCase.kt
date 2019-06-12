/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetSelectedRouteTripsUseCase @Inject constructor(private val repository: TripsRepository) {
    suspend operator fun invoke(stopId: StopId, selectedRoutes: Array<RouteSelection>): Flow<List<TripAdapterItem>> =
        repository.getResultCache(stopId)
            .asFlow()
            .flowOn(Dispatchers.Default)
            .map { response -> transformApiResponse(response, selectedRoutes) }
            .flowOn(Dispatchers.Main)

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