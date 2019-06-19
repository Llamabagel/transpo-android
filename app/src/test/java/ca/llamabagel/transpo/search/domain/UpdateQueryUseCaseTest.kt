/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.domain

import ca.llamabagel.transpo.data.provideFakeSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class UpdateQueryUseCaseTest {
    private val repository = provideFakeSearchRepository()
    private val updateQueryUseCase = UpdateQueryUseCase(repository)

    @Test
    fun `when updateQueryUseCase is invoked, search results update`() = runBlocking {
        updateQueryUseCase("Walkley")
        assert(repository.stopFlow.first().isNotEmpty())
    }
}