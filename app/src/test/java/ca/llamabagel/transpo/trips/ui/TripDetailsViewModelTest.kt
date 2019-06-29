package ca.llamabagel.transpo.trips.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.trips.domain.GetTripDetailsUseCase
import ca.llamabagel.transpo.trips.domain.UpdateTripDataUseCase
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class TripDetailsViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeTripsRepository()

    private val viewModel = TripDetailsViewModel(
        GetTripDetailsUseCase(repository, provideFakeCoroutinesDispatcherProvider())
    )

    @Test
    fun `when trips updated then data is updated`() = runBlocking {
        viewModel.setStop(TestStops.walkleyJasper.id, SingleTrip("44", 0, "17:37"))

        val updateTripData = UpdateTripDataUseCase(repository)
        updateTripData(TestStops.walkleyJasper.id)

        delay(100)

        assertNotNull(viewModel.tripData.value)
    }
}