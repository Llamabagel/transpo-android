/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class UpdateTripDataUseCaseTest {

    private val repository = provideFakeTripsRepository()
    private val updateTripData = UpdateTripDataUseCase(repository)

    @Test
    fun `when trips updated then new data is broadcast`() = runBlocking {
        updateTripData(TestStops.walkleyJasper.id)

        val flow = repository.getResultCache(TestStops.walkleyJasper.id).asFlow()
        assertEquals(TestStops.walkleyJasper.code.value, flow.first().stopCode)
    }
}