/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripsAdapter
import ca.llamabagel.transpo.utils.TAG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripsViewModel @Inject constructor(
    private val getStop: GetStopUseCase,
    private val updateTripData: UpdateTripDataUseCase,
    private val getNextBusTrips: GetNextBusTripsUseCase,
    private val clearStopCache: ClearStopCacheUseCase
) : ViewModel() {

    private lateinit var stopId: String

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _stop = MutableLiveData<Stop?>()
    val stop: LiveData<Stop?> = _stop

    private val _displayData = MutableLiveData<List<TripAdapterItem>>()
    val displayData: LiveData<List<TripAdapterItem>> = _displayData

    private val selectedRoutes = mutableSetOf<RouteSelection>()

    private val _viewerData = MutableLiveData<List<TripAdapterItem>>()
    val viewerData: LiveData<List<TripAdapterItem>> = _viewerData

    fun loadStop(stopId: String) = viewModelScope.launch {
        this@TripsViewModel.stopId = stopId
        _stop.value = (getStop(stopId) as? Result.Success)?.data

        viewModelScope.launch {
            getNextBusTrips(_stop.value!!.code).collect {
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
        updateViewerData()
    }

    fun clearSelection() {
        selectedRoutes.clear()
        _viewerData.value = emptyList()
    }

    private fun updateViewerData() {
        /*if (selectedRoutes.isEmpty()) return

        val routeData = (apiData as Result.Success).data.routes
        _viewerData.value = routeData
            .filter { route -> selectedRoutes.contains(RouteSelection(route.number, route.directionId)) }
            .flatMap { route -> route.trips.map { trip -> TripUiModel(route, trip) } }
            .sortedBy { model -> model.trip.adjustedScheduleTime }
            .map(::TripItem)*/
    }

    override fun onCleared() {
        super.onCleared()
        if (_stop.value != null) {
            clearStopCache(_stop.value!!.code)
        }
    }
}

/**
 * Represents the selection of a route, with its corresponding directionId
 * from the [TripsAdapter].
 */
data class RouteSelection(val number: String, val directionId: Int)