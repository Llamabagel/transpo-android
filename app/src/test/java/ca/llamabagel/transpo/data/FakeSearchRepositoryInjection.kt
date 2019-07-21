/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.search.data.SearchRepository
import ca.llamabagel.transpo.utils.FakeStringUtils
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
fun provideFakeSearchRepository() = SearchRepository(
    getTransitDatabase(),
    FakeStringUtils(),
    provideFakeCoroutinesDispatcherProvider(),
    FakeGeocoder()
)