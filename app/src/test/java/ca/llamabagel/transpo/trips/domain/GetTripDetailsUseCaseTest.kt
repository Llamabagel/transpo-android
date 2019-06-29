package ca.llamabagel.transpo.trips.domain

import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.trips.ui.SingleTrip
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetTripDetailsUseCaseTest {

    private val repository = provideFakeTripsRepository()

    private val getTripDetails = GetTripDetailsUseCase(repository, provideFakeCoroutinesDispatcherProvider())

    @Test
    fun `when trip data updated then details are broadcast`() = runBlocking {
        val flow = getTripDetails(TestStops.walkleyJasper.id, SingleTrip("44", 0, "17:37"))
        repository.getTrips(TestStops.walkleyJasper.id)

        assertNotNull(flow.first())
    }
}