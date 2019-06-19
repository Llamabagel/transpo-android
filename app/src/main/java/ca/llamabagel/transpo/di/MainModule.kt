/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import ca.llamabagel.transpo.home.MainActivity
import ca.llamabagel.transpo.home.MainViewModel
import ca.llamabagel.transpo.home.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * TODO: Remove this and implement the proper modules for the background service
 */
@Module(includes = [DataModule::class])
abstract class MainModule {

    @Binds
    abstract fun mainActivityAsFragmentActivity(activity: MainActivity): FragmentActivity

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun mainViewModel(
            factory: MainViewModelFactory,
            fragmentActivity: FragmentActivity
        ): MainViewModel {
            return ViewModelProviders.of(fragmentActivity, factory).get(MainViewModel::class.java)
        }
    }
}