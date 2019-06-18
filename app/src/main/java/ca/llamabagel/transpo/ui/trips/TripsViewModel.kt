/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripsAdapter
import ca.llamabagel.transpo.utils.TAG
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TripsViewModel @Inject constructor(
    private val getStop: GetStopUseCase,
    private val updateTripData: UpdateTripDataUseCase,
    private val getNextBusTrips: GetNextBusTripsUseCase,
    private val clearStopCache: ClearStopCacheUseCase
) : ViewModel() {

    private var stopId: StopId = StopId("")

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _stop = MutableLiveData<Stop?>()
    val stop: LiveData<Stop?> = _stop

    private val _displayData = MutableLiveData<List<TripAdapterItem>>()
    val displayData: LiveData<List<TripAdapterItem>> = _displayData

    private val selectedRoutes = mutableSetOf<RouteSelection>()

    private val _viewerData = MutableLiveData<List<TripAdapterItem>>()
    val viewerData: LiveData<List<TripAdapterItem>> = _viewerData

    fun loadStop(id: String) {
        stopId = StopId(id)
        viewModelScope.launch {
            _stop.value = (getStop(stopId) as? Result.Success)?.data

            getNextBusTrips(stopId).collect {
                runBlocking {
                    _displayData.value = it
                    println("Test DatA")
                }
            }
        }
    }

    fun getTrips() {
        if (_stop.value == null) {
            Log.i(TAG, "No stop loaded")
            return
        }

        _isRefreshing.value = true
        viewModelScope.launch {
            when (updateTripData(stopId)) {
                is Result.Success -> {
                }
                is Result.Error -> {
                    // TODO: Handle errors
                }
            }

            _isRefreshing.value = false
        }
    }

    fun updateRouteSelection(number: String, directionId: Int, selected: Boolean) {
        when {
            selected -> selectedRoutes.add(RouteSelection(number, directionId))
            else -> selectedRoutes.remove(RouteSelection(number, directionId))
        }
    }

    fun clearSelection() {
        selectedRoutes.clear()
        _viewerData.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        if (_stop.value != null) {
            clearStopCache(stopId)
        }
    }
}

/**
 * Represents the selection of a route, with its corresponding directionId
 * from the [TripsAdapter].
 */
@Parcelize
data class RouteSelection(val number: String, val directionId: Int) : Parcelable