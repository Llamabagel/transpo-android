/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GetNextBusTripsUseCaseTest {

    private val repository = provideFakeTripsRepository()
    private val getNextBusTrips = GetNextBusTripsUseCase(repository, provideFakeCoroutinesDispatcherProvider())

    @Test
    fun `get bus trips simple`() = runBlocking {
        val flow = getNextBusTrips(TestStops.walkleyJasper.id)

        repository.getTrips(TestStops.walkleyJasper.id)

        val trips = flow.first()
        assertEquals(3, trips.size)
    }

    @Test
    fun `get bus trips complex`() = runBlocking {
        val flow = getNextBusTrips(TestStops.mackenzieKing.id)

        repository.getTrips(TestStops.mackenzieKing.id)

        val trips = flow.first()
        assertEquals(44, trips.size)
        assertEquals(2, (trips[0] as TripItem).trip.adjustedScheduleTime)
        assertEquals(4, (trips[1] as TripItem).trip.adjustedScheduleTime)
    }
}