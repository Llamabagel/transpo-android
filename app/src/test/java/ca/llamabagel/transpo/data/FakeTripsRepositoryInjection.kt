/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider
import com.nhaarman.mockitokotlin2.mock

private val mockServer = createMockServer()

fun provideFakeTripsRepository(): TripsRepository =
    TripsRepository(
        provideFakeTransitDatabase(),
        createTestTripsService(mockServer),
        mock(),
        provideFakeCoroutinesDispatcherProvider()
    )