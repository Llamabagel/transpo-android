package ca.llamabagel.transpo.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ca.llamabagel.transpo.data.provideFakeLiveUpdatesRepository
import ca.llamabagel.transpo.home.domain.GetLiveUpdatesDataUseCase
import ca.llamabagel.transpo.home.domain.UpdateLiveUpdatesUseCase
import ca.llamabagel.transpo.utils.CoroutinesTestRule
import org.junit.Rule
import org.junit.rules.RuleChain

/**
 * TODO: Postponed until WorkManager and automatic data updater is reworked
 * Can't initialize HomeViewModel without a proper WorkManager.
 */
class HomeViewModelTest {

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(CoroutinesTestRule()).around(InstantTaskExecutorRule())

    private val repository = provideFakeLiveUpdatesRepository()

    private val viewModel = HomeViewModel(
        GetLiveUpdatesDataUseCase(repository),
        UpdateLiveUpdatesUseCase(repository)
    )

    /*@Test
    fun `when data refreshed then new data is emitted`() {
        viewModel.refreshLiveUpdates()

        assertTrue(viewModel.updateData.value!!.isNotEmpty())
    }*/
}