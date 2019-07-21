package ca.llamabagel.transpo.home.domain

import ca.llamabagel.transpo.home.data.LiveUpdatesRepository
import javax.inject.Inject

class UpdateLiveUpdatesUseCase @Inject constructor(private val liveUpdatesRepository: LiveUpdatesRepository) {
    suspend operator fun invoke() = liveUpdatesRepository.updateLiveUpdates()
}