/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.models.trips.ApiResponse
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
        assertEquals(TestStops.walkleyJasper.code.value, (flow.first() as Result.Success<ApiResponse>).data.stopCode)
    }
}