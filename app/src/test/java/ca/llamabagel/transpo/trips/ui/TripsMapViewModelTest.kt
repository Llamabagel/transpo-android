package ca.llamabagel.transpo.trips.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.trips.domain.GetSelectedRouteTripsUseCase
import ca.llamabagel.transpo.trips.domain.UpdateTripDataUseCase
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class TripsMapViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeTripsRepository()

    private val viewModel = TripsMapViewModel(
        GetSelectedRouteTripsUseCase(repository, provideFakeCoroutinesDispatcherProvider())
    )

    @Test
    fun `when trips updated then data is updated`() = runBlocking {
        viewModel.setStop(TestStops.mackenzieKing.id, arrayOf(RouteSelection("44", 0)))

        val updateTripData = UpdateTripDataUseCase(repository)
        updateTripData(TestStops.mackenzieKing.id)

        delay(100)

        assertNotNull(viewModel.viewerData.value)
    }
}