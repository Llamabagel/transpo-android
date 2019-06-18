/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetNextBusTripsUseCaseTest {

    private val repository = provideFakeTripsRepository()
    private val getNextBusTrips = GetNextBusTripsUseCase(repository, provideFakeCoroutinesDispatcherProvider())

    @Test
    fun `when get bust trips then number of trips is correct`() = runBlocking {
        val flow = getNextBusTrips(TestStops.walkleyJasper.id)

        repository.getTrips(TestStops.walkleyJasper.id)

        val trips = flow.first()
        assertEquals(3, trips.size)
    }

    @Test
    fun `when get bus trips then order of trips is correct`() = runBlocking {
        val flow = getNextBusTrips(TestStops.mackenzieKing.id)

        repository.getTrips(TestStops.mackenzieKing.id)

        val trips = flow.first()
        // Verify sorted order of trips
        assertTrue(trips.filterIsInstance<TripItem>().zipWithNext().all { (first, second) ->
            first.trip.adjustedScheduleTime <= second.trip.adjustedScheduleTime
        })
    }
}