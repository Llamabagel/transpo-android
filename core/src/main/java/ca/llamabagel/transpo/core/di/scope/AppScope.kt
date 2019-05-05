/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.di.scope

import javax.inject.Scope

/**
 * Scope for the lifetime of the entire app.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope