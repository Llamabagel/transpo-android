/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.llamabagel.transpo.trips.data.TripsRepository
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.models.trips.ApiResponse
import javax.inject.Inject

class TripsViewModel @Inject constructor(private val tripsRepository: TripsRepository) : ViewModel() {

    private val _stop = MutableLiveData<Stop>()
    val stop: LiveData<Stop> = _stop

    private val _apiResponse = MutableLiveData<ApiResponse>()
    val apiResponse: LiveData<ApiResponse> = _apiResponse

    fun loadStop(stopId: String) {

    }

}