/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import ca.llamabagel.transpo.TranspoApplication
import ca.llamabagel.transpo.di.scope.FeatureScope
import dagger.Component

@Component(
    modules = [DataModule::class, SharedPreferencesModule::class, TransitDatabaseModule::class, WorkerModule::class, AssistedWorkerInjectModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface WorkerComponent : BaseComponent<TranspoApplication> {
    fun factory(): InjectionWorkerFactory

    @Component.Builder
    interface Builder {
        fun build(): WorkerComponent

        fun coreComponent(component: CoreComponent): Builder
        fun sharedPreferencesModule(module: SharedPreferencesModule): Builder
        fun transitDatabaseModule(module: TransitDatabaseModule): Builder
    }
}