package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.data.provideFakeSearchRepository
import ca.llamabagel.transpo.search.data.SearchFilter
import ca.llamabagel.transpo.search.data.SearchFilters.*
import ca.llamabagel.transpo.search.ui.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalArgumentException

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SetRecentSearchResultUseCaseTest {
    private val repository = provideFakeSearchRepository()
    private val setRecentSearchResults = SetRecentSearchResultUseCase(repository)

    @Test
    fun `when route item is clicked, route item is added to recent searches database`() = runBlockingTest {
        setRecentSearchResults(SearchResult.RouteItem("name", "55", "name", "sdf87"))
        repository.getSearchResults("55", SearchFilter())
        assertEquals(
            listOf(SearchResult.RecentItem("name", "name", "55", null, ROUTE, "sdf87")),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when stop item is clicked, stop item is added to recent searches database`() = runBlockingTest {
        setRecentSearchResults(SearchResult.StopItem("Hurdman", "3023", "", "hrgd65"))
        repository.getSearchResults("Hurdman", SearchFilter())
        assertEquals(
            listOf(SearchResult.RecentItem("Hurdman", "", null, "3023", STOP, "hrgd65")),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when place item is clicked, place item is added to recent searches database`() = runBlockingTest {
        setRecentSearchResults(SearchResult.PlaceItem("24 Sussex", "Ottawa", "address-987242342"))
        repository.getSearchResults("24 Sussex", SearchFilter())
        assertEquals(
            listOf(SearchResult.RecentItem("24 Sussex", "Ottawa", null, null, PLACE, "address-987242342")),
            repository.recentFlow.first()
        )
    }

    @Test
    fun `when recent item is clicked, recent item is added to the recent searches database`() = runBlockingTest {
        setRecentSearchResults(SearchResult.RecentItem("24 Sussex", "Ottawa", null, null, PLACE, "address-987242342"))
        repository.getSearchResults("24 Sussex", SearchFilter())
        assertEquals(
            listOf(SearchResult.RecentItem("24 Sussex", "Ottawa", null, null, PLACE, "address-987242342")),
            repository.recentFlow.first()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when other item type is clicked, illegal argument exception is thrown`() = runBlockingTest {
        setRecentSearchResults(SearchResult.CategoryHeader(""))
    }
}