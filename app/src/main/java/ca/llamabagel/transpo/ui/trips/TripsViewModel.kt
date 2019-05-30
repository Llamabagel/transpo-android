/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.utils.TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripsViewModel @Inject constructor(private val tripsRepository: TripsRepository) : ViewModel() {

    private val _stop = MutableLiveData<Stop?>()
    val stop: LiveData<Stop?> = _stop

    private val _displayData = MutableLiveData<List<TripAdapterItem>>()
    val displayData: LiveData<List<TripAdapterItem>> = _displayData

    fun loadStop(stopId: String) = viewModelScope.launch {
        _stop.value = tripsRepository.getStop(stopId)
    }

    fun getTrips() = viewModelScope.launch {
        if (_stop.value == null) {
            Log.i(TAG, "No stop loaded")
        }

        val response = tripsRepository.getTrips(_stop.value!!.code)

        _displayData.value = response.routes.flatMap { route ->
            route.trips.map { trip -> TripUiModel(route, trip) }
        }
            .sortedBy { it.adjustedScheduleTime }
            .map(::TripItem)
    }
}