/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di.search

import ca.llamabagel.transpo.di.BaseActivityComponent
import ca.llamabagel.transpo.di.CoreComponent
import ca.llamabagel.transpo.di.scope.FeatureScope
import ca.llamabagel.transpo.ui.search.SearchActivity
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [SearchModule::class], dependencies = [CoreComponent::class])
abstract class SearchComponent :
    BaseActivityComponent<SearchActivity> {

    @Component.Builder
    interface Builder {
        fun build(): SearchComponent

        @BindsInstance
        fun searchActivity(searchActivity: SearchActivity): Builder

        fun coreComponent(component: CoreComponent): Builder
    }

}