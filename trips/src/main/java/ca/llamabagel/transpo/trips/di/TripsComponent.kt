/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.di

import ca.llamabagel.transpo.di.BaseActivityComponent
import ca.llamabagel.transpo.di.CoreComponent
import ca.llamabagel.transpo.di.TransitDatabaseModule
import ca.llamabagel.transpo.di.scope.FeatureScope
import ca.llamabagel.transpo.trips.ui.TripsActivity
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(modules = [TripsModule::class, TransitDatabaseModule::class], dependencies = [CoreComponent::class])
abstract class TripsComponent :
    BaseActivityComponent<TripsActivity> {

    @Component.Builder
    interface Builder {
        fun build(): TripsComponent

        @BindsInstance
        fun tripsActivity(tripsActivity: TripsActivity): Builder

        fun coreComponent(component: CoreComponent): Builder
        fun transitDatabaseModule(module: TransitDatabaseModule): Builder
    }

}