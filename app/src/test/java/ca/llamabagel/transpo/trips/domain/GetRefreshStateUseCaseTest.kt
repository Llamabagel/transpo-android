package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.models.trips.ApiResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test

class GetRefreshStateUseCaseTest {

    private val repository = provideFakeTripsRepository()

    private val getRefreshState = GetRefreshStateUseCase(repository)

    @Test
    fun `when loading state offered then loading is true`() = runBlockingTest {
        val cache = repository.getResultCache(TestStops.mackenzieKing.id)
        cache.offer(Result.Loading())

        assertTrue(getRefreshState(TestStops.mackenzieKing.id).first())
    }

    @Test
    fun `when success state offered then loading is false`() = runBlockingTest {
        val cache = repository.getResultCache(TestStops.mackenzieKing.id)
        cache.offer(Result.Success(ApiResponse("Whatever", emptyList())))

        assertFalse(getRefreshState(TestStops.mackenzieKing.id).first())
    }

    @Test
    fun `when error state offered then loading is false`() = runBlockingTest {
        val cache = repository.getResultCache(TestStops.mackenzieKing.id)
        cache.offer(Result.Error(IllegalStateException("You dun goofed")))

        assertFalse(getRefreshState(TestStops.mackenzieKing.id).first())
    }
}