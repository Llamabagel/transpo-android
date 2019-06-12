/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.api.TripsService
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TripsRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository = provideFakeTripsRepository()

    @Test
    fun `get stop returns success`() = runBlockingTest {
        val result = repository.getStop(TestStops.walkleyJasper.id)

        assertEquals(Result.Success(TestStops.walkleyJasper), result)
    }

    @Test
    fun `get stop returns error`() = runBlockingTest {
        val result = repository.getStop(StopId("NOT A REAL STOP"))

        assertTrue(result is Result.Error)
    }

    @Test
    fun `api responses broadcast`() = runBlocking<Unit> {
        val broadcastChannel = repository.getResultCache(TestStops.mackenzieKing1A.id)

        val result = repository.getTrips(TestStops.mackenzieKing1A.id)

        assertTrue(result is Result.Success)
        assertNotNull(broadcastChannel.valueOrNull)
    }

    @Test
    fun `clear cache removes channel`() = runBlockingTest {
        val channel = repository.getResultCache(TestStops.walkleyJasper.id)

        repository.clearCacheFor(TestStops.walkleyJasper.id)
        val newChannel = repository.getResultCache(TestStops.walkleyJasper.id)

        assertNotSame(channel, newChannel)
    }
}