/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.trips.domain.GetSelectedRouteTripsUseCase
import ca.llamabagel.transpo.trips.ui.adapter.TripAdapterItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripsMapViewModel @Inject constructor(
    private val getSelectedRouteTrips: GetSelectedRouteTripsUseCase
) : ViewModel() {

    private var stopId: StopId = StopId.DEFAULT

    private val _viewerData = MutableLiveData<List<TripAdapterItem>>()
    val viewerData: LiveData<List<TripAdapterItem>> = _viewerData

    fun setStop(stopId: StopId, selectedRoutes: Array<RouteSelection>) {
        this.stopId = stopId

        viewModelScope.launch {
            getSelectedRouteTrips(stopId, selectedRoutes).collect {
                _viewerData.value = it
            }
        }
    }
}