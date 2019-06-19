/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.trips.ui.adapter.TripAdapterItem
import ca.llamabagel.transpo.trips.ui.adapter.TripsAdapter
import ca.llamabagel.transpo.trips.domain.ClearStopCacheUseCase
import ca.llamabagel.transpo.trips.domain.GetNextBusTripsUseCase
import ca.llamabagel.transpo.trips.domain.GetStopUseCase
import ca.llamabagel.transpo.trips.domain.UpdateTripDataUseCase
import ca.llamabagel.transpo.utils.TAG
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    fun loadStop(id: String) = viewModelScope.launch {
        stopId = StopId(id)
        _stop.value = (getStop(stopId) as? Result.Success)?.data

        viewModelScope.launch {
            getNextBusTrips(stopId).collect {
                _displayData.postValue(it)
            }
        }
    }

    fun getTrips() = viewModelScope.launch {
        if (_stop.value == null) {
            Log.i(TAG, "No stop loaded")
            return@launch
        }

        _isRefreshing.value = true
        when (updateTripData(stopId)) {
            is Result.Success -> {
            }
            is Result.Error -> {
                // TODO: Handle errors
            }
        }

        _isRefreshing.value = false
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