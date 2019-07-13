/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.search.data.SearchFilter
import ca.llamabagel.transpo.search.data.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UpdateQueryUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    suspend operator fun invoke(query: String, filters: SearchFilter) =
        searchRepository.getSearchResults(query, filters)
}