/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ClearStopCacheUseCaseTest {

    private val repository = provideFakeTripsRepository()
    private val clearStopCache = ClearStopCacheUseCase(repository)

    @Test
    fun `when stop cache cleared then old cache is removed`() = runBlocking<Unit> {
        val initialCache = repository.getResultCache(TestStops.mackenzieKing1A.id)
        clearStopCache(TestStops.mackenzieKing1A.id)
        val newCache = repository.getResultCache(TestStops.mackenzieKing1A.id)

        assertNotSame(initialCache, newCache)
    }
}