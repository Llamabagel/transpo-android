/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import ca.llamabagel.transpo.data.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UpdateQueryUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    suspend operator fun invoke(query: String) = searchRepository.getSearchResults(query)
}