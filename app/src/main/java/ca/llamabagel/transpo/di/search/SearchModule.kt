/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di.search

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import ca.llamabagel.transpo.ui.search.SearchActivity
import ca.llamabagel.transpo.ui.search.SearchViewModel
import ca.llamabagel.transpo.ui.search.SearchViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class SearchModule {

    @Binds
    abstract fun searchActivityAsFragmentActivity(activity: SearchActivity): FragmentActivity

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun searchViewModel(
            factory: SearchViewModelFactory,
            fragmentActivity: FragmentActivity
        ): SearchViewModel {
            return ViewModelProviders.of(fragmentActivity, factory)[SearchViewModel::class.java]
        }

    }
}