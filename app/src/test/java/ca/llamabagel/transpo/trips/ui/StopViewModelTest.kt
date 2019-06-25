package ca.llamabagel.transpo.trips.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.trips.domain.*
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class StopViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeTripsRepository()

    private val viewModel = StopViewModel(
        GetNextBusTripsUseCase(repository, provideFakeCoroutinesDispatcherProvider())
    )

    @Test
    fun `when trips updated then data is updated`() = runBlocking {
        viewModel.setStop(TestStops.mackenzieKing.id)

        val updateTripData = UpdateTripDataUseCase(repository)
        updateTripData(TestStops.mackenzieKing.id)

        // TODO: See if Flow can be tested synchronously correctly
        delay(100)

        assertNotNull(viewModel.resultsData.value)
    }
}