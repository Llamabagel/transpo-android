/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import ca.llamabagel.transpo.di.scope.FeatureScope
import ca.llamabagel.transpo.ui.home.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [MainModule::class, DataModule::class, SharedPreferencesModule::class, TransitDatabaseModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface DataComponent : BaseActivityComponent<MainActivity> {

    @Component.Builder
    interface Builder {
        fun build(): DataComponent
        // TODO: This is for temporary testing. Will eventually get its own service, or something
        @BindsInstance
        fun mainActivity(activity: MainActivity): Builder

        fun coreComponent(component: CoreComponent): Builder
        fun sharedPreferencesModule(module: SharedPreferencesModule): Builder
        fun transitDatabaseModule(module: TransitDatabaseModule): Builder
    }

}