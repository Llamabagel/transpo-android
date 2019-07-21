package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.home.data.LiveUpdatesRepository
import ca.llamabagel.transpo.utils.provideFakeCoroutinesDispatcherProvider

fun provideFakeLiveUpdatesRepository() = LiveUpdatesRepository(
    createTestTripsService(createMockServer()),
    getTransitDatabase(),
    provideFakeCoroutinesDispatcherProvider()
)