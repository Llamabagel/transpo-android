package ca.llamabagel.transpo.home.data

import ca.llamabagel.transpo.data.provideFakeLiveUpdatesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class LiveUpdatesRepositoryTest {

    private val repository = provideFakeLiveUpdatesRepository()

    @Test
    fun `when live updates updated then new values are dispatched`() = runBlocking {
        val feed = repository.getLiveUpdatesFlow()
        repository.updateLiveUpdates()

        assertTrue(feed.first().isNotEmpty())
    }
}
