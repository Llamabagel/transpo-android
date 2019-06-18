/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.test.TestCoroutineDispatcher

fun provideFakeCoroutinesDispatcherProvider(): CoroutinesDispatcherProvider =
    CoroutinesDispatcherProvider(TestCoroutineDispatcher(), Unconfined, Unconfined)