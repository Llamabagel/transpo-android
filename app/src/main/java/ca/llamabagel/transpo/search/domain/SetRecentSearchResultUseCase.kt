package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.search.data.SearchRepository
import ca.llamabagel.transpo.search.ui.viewholders.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class SetRecentSearchResultUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend operator fun invoke(item: SearchResult) {
        when (item) {
            is RouteResult -> repository.pushRecent(
                item.name,
                item.routeType,
                item.number,
                null,
                item.id,
                item.type
            )
            is StopResult -> repository.pushRecent(
                item.name,
                item.routes,
                null,
                item.code,
                item.id,
                item.type
            )
            is PlaceResult -> repository.pushRecent(
                item.primary,
                item.secondary,
                null,
                null,
                item.id,
                item.type
            )
            is RecentResult -> repository.pushRecent(
                item.primary,
                item.secondary,
                item.number,
                item.code,
                item.id,
                item.type
            )
            else -> throw IllegalArgumentException("Can't handle item of type ${item.type}")
        }
    }
}