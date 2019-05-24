/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di.search

import ca.llamabagel.transpo.coreComponent
import ca.llamabagel.transpo.ui.search.SearchActivity

fun inject(activity: SearchActivity) {
    DaggerSearchComponent.builder()
        .searchActivity(activity)
        .coreComponent(activity.coreComponent())
        .build()
        .inject(activity)
}