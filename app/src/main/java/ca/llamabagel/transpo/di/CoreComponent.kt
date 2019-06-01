/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import ca.llamabagel.transpo.TranspoApplication
import ca.llamabagel.transpo.ui.home.HomeViewModel
import ca.llamabagel.transpo.ui.map.MapViewModel
import ca.llamabagel.transpo.ui.search.SearchViewModel
import ca.llamabagel.transpo.ui.trips.TripsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        CoreModule::class,
        DataModule::class,
        SharedPreferencesModule::class,
        TransitDatabaseModule::class,
        WorkerModule::class,
        AssistedWorkerInjectModule::class,
        TripsModule::class
    ]
)
@Singleton
interface CoreComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun sharedPreferenceModule(sharedPreferencesModule: SharedPreferencesModule): Builder

        fun build(): CoreComponent
    }

    fun factory(): InjectionWorkerFactory

    fun homeViewModelFactory(): ViewModelFactory<HomeViewModel>
    fun tripsViewModelFactory(): ViewModelFactory<TripsViewModel>
    fun searchViewModelFactory(): ViewModelFactory<SearchViewModel>
    fun mapViewModelFactory(): ViewModelFactory<MapViewModel>

    fun inject(application: TranspoApplication)
}