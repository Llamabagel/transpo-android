package ca.llamabagel.transpo.trips.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.TestStops
import ca.llamabagel.transpo.data.provideFakeTripsRepository
import ca.llamabagel.transpo.trips.domain.ClearStopCacheUseCase
import ca.llamabagel.transpo.trips.domain.GetRefreshStateUseCase
import ca.llamabagel.transpo.trips.domain.GetStopUseCase
import ca.llamabagel.transpo.trips.domain.UpdateTripDataUseCase
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class TripsViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeTripsRepository()

    private val viewModel = TripsViewModel(
        GetStopUseCase(repository),
        UpdateTripDataUseCase(repository),
        ClearStopCacheUseCase(repository),
        GetRefreshStateUseCase(repository)
    )

    @Test
    fun `when stop loaded then stop data is available`() {
        viewModel.loadStop(TestStops.mackenzieKing.id.value)

        assertEquals(TestStops.mackenzieKing, viewModel.stop.value)
    }

    @Test
    fun `when loading then refresh state is updated`() = runBlockingTest {
        viewModel.loadStop(TestStops.mackenzieKing.id.value)

        val channel = repository.getResultCache(TestStops.mackenzieKing.id)
        channel.offer(Result.Loading())

        assertTrue(viewModel.isRefreshing.value == true)
    }
}