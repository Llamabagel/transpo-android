/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.llamabagel.transpo.data.TripsRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class TripsViewModelFactory @Inject constructor(
    private val tripsRepository: TripsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != TripsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        return TripsViewModel(tripsRepository) as T
    }

}