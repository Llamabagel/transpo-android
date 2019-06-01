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
import ca.llamabagel.transpo.data.TripsRepository
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.utils.TAG
import ca.llamabagel.transpo.ui.trips.adapter.TripsAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripsViewModel @Inject constructor(private val tripsRepository: TripsRepository) : ViewModel() {

    private lateinit var apiData: Result<ApiResponse>

    private val _stop = MutableLiveData<Stop?>()
    val stop: LiveData<Stop?> = _stop

    private val _displayData = MutableLiveData<List<TripAdapterItem>>()
    val displayData: LiveData<List<TripAdapterItem>> = _displayData

    private val selectedRoutes = mutableSetOf<RouteSelection>()

    private val _viewerData = MutableLiveData<List<TripAdapterItem>>()
    val viewerData: LiveData<List<TripAdapterItem>> = _viewerData

    fun loadStop(stopId: String) = viewModelScope.launch {
        _stop.value = tripsRepository.getStop(stopId)
    }

    fun getTrips() = viewModelScope.launch {
        if (_stop.value == null) {
            Log.i(TAG, "No stop loaded")
        }

        apiData = tripsRepository.getTrips(_stop.value!!.code)

        when (val copy = apiData) {
            is Result.Success -> {
                _displayData.value = copy.data.routes.flatMap { route ->
                    route.trips.map { trip -> TripUiModel(route, trip) }
                }
                    .sortedBy { it.trip.adjustedScheduleTime }
                    .map(::TripItem)
                updateViewerData()
            }
            is Result.Error -> {
                // TODO: Handle errors
            }
        }
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
        if (selectedRoutes.isEmpty()) return

        val routeData = (apiData as Result.Success).data.routes
        _viewerData.value = routeData
            .filter { route -> selectedRoutes.contains(RouteSelection(route.number, route.directionId)) }
            .flatMap { route -> route.trips.map { trip -> TripUiModel(route, trip) } }
            .sortedBy { model -> model.trip.adjustedScheduleTime }
            .map(::TripItem)
    }
}

/**
 * Represents the selection of a route, with its corresponding directionId
 * from the [TripsAdapter].
 */
data class RouteSelection(val number: String, val directionId: Int)