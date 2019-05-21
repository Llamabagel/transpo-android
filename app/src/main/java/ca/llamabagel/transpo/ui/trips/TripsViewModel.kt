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

    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse: LiveData<ApiResponse> = _apiResponse

    fun loadStop(stopId: String) = viewModelScope.launch {
        _stop.value = tripsRepository.getStop(stopId)
    }

    fun getTrips() = viewModelScope.launch {
        if (_stop.value == null) {
            Log.i(TAG, "No stop loaded")
        }
        _apiResponse.value = tripsRepository.getTrips(_stop.value!!.code)
    }

}