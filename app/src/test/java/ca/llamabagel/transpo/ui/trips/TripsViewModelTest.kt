/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.Assert.*


class TripsViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeTripsRepository()
    private val viewModel = TripsViewModel(
        GetStopUseCase(repository),
        UpdateTripDataUseCase(repository),
        GetNextBusTripsUseCase(repository, provideFakeCoroutinesDispatcherProvider()),
        ClearStopCacheUseCase(repository)
    )

    @Test
    fun `when stop loaded then stop data is available`() {
        viewModel.loadStop(TestStops.mackenzieKing.id.value)

        assertEquals(TestStops.mackenzieKing, viewModel.stop.value)
    }

    @Test
    fun `when trips loaded then trip data is available`() {
        viewModel.loadStop(TestStops.mackenzieKing.id.value)
        viewModel.getTrips()

        assertNotNull(viewModel.displayData.value)
    }

}