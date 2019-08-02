package ca.llamabagel.transpo.map.domain

import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.map.data.MapRepository
import javax.inject.Inject

class GetStopDetailUseCase @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke(id: StopId): Stop? = repository.getStop(id)
}