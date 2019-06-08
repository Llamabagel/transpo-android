/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

inline fun <reified T> Any.stubReturn(stub: T): OngoingStubbing<Any> = Mockito.`when`(this).thenReturn(stub)