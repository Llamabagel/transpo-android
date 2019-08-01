/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import ca.llamabagel.transpo.TranspoApplication
import ca.llamabagel.transpo.home.ui.HomeViewModel
import ca.llamabagel.transpo.map.ui.MapViewModel
import ca.llamabagel.transpo.search.ui.SearchViewModel
import ca.llamabagel.transpo.settings.data.AppSettings
import ca.llamabagel.transpo.trips.ui.StopViewModel
import ca.llamabagel.transpo.trips.ui.TripDetailsViewModel
import ca.llamabagel.transpo.trips.ui.TripsMapViewModel
import ca.llamabagel.transpo.trips.ui.TripsViewModel
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
        AssistedWorkerInjectModule::class
    ]
)
@Singleton
interface CoreComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): CoreComponent
    }

    fun factory(): InjectionWorkerFactory

    fun homeViewModelFactory(): ViewModelFactory<HomeViewModel>
    fun tripsViewModelFactory(): ViewModelFactory<TripsViewModel>
    fun stopViewModelFactory(): ViewModelFactory<StopViewModel>
    fun tripsMapViewModelFactory(): ViewModelFactory<TripsMapViewModel>
    fun tripDetailsViewModelFactory(): ViewModelFactory<TripDetailsViewModel>
    fun searchViewModelFactory(): ViewModelFactory<SearchViewModel>
    fun mapViewModelFactory(): ViewModelFactory<MapViewModel>

    fun appSettings(): AppSettings

    fun inject(application: TranspoApplication)
}