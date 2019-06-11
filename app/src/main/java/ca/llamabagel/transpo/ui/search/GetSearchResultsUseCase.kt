/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import ca.llamabagel.transpo.data.SearchRepository
import ca.llamabagel.transpo.ui.search.viewholders.SearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetSearchResultsUseCase @Inject constructor(private val repository: SearchRepository) {

    @FlowPreview
    operator fun invoke(): Flow<List<SearchResult>> = repository.searchResult.asFlow()

}