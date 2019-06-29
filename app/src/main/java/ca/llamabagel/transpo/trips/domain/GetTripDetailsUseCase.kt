package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.models.trips.Trip
import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.trips.ui.SingleTrip
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetTripDetailsUseCase @Inject constructor(
    private val repository: TripsRepository,
    private val dispatchers: CoroutinesDispatcherProvider
) {

    suspend operator fun invoke(stopId: StopId, trip: SingleTrip): Flow<Trip?> {
        return repository.getResultCache(stopId)
            .asFlow()
            .map { result -> result.data?.let { findMatchingTrip(it, trip) } }
            .flowOn(dispatchers.computation)
    }

    private fun findMatchingTrip(apiResponse: ApiResponse, singleTrip: SingleTrip): Trip? {
        return apiResponse.routes.find { (number, directionId) ->
            singleTrip.routeNumber == number && directionId == singleTrip.directionId
        }?.let { route ->
            route.trips.find { it.startTime == singleTrip.startTime }
        }
    }
}