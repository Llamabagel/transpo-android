package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.trips.data.TripsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRefreshStateUseCase @Inject constructor(
    private val repository: TripsRepository
) {
    suspend operator fun invoke(stopId: StopId): Flow<Boolean> =
        repository.getResultCache(stopId)
            .asFlow()
            .map { state -> state is Result.Loading }
}