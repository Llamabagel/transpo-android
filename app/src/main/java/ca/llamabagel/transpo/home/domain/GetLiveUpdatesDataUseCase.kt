package ca.llamabagel.transpo.home.domain

import ca.llamabagel.transpo.home.data.LiveUpdatesRepository
import javax.inject.Inject

class GetLiveUpdatesDataUseCase @Inject constructor(private val liveUpdatesRepository: LiveUpdatesRepository) {
    operator fun invoke() = liveUpdatesRepository.getLiveUpdatesFlow()
}