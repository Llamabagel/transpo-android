/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.utils.FakeStringsGen
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
fun provideFakeSearchRepository() = SearchRepository(
    provideFakeTransitDatabase(),
    FakeStringsGen(),
    provideFakeCoroutinesDispatcherProvider(),
    FakeGeocoder()
)