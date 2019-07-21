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
                item.name.toString(),
                item.routeType.toString(),
                item.number.toString(),
                null,
                item.id,
                item.type
            )
            is StopResult -> repository.pushRecent(
                item.name.toString(),
                item.routes.toString(),
                null,
                item.code.toString(),
                item.id,
                item.type
            )
            is PlaceResult -> repository.pushRecent(
                item.primary.toString(),
                item.secondary.toString(),
                null,
                null,
                item.id,
                item.type
            )
            is RecentResult -> repository.pushRecent(
                item.primary.toString(),
                item.secondary.toString(),
                item.number?.toString(),
                item.code?.toString(),
                item.id,
                item.type
            )
            else -> throw IllegalArgumentException("Can't handle item of type ${item.type}")
        }
    }
}