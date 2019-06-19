/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.TransitDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapViewModel @Inject constructor(private val transitDatabase: TransitDatabase) : ViewModel() {

    private val _stops = MutableLiveData<List<Stop>>()
    val stops: LiveData<List<Stop>> = _stops

    fun getStops() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _stops.postValue(transitDatabase.stopQueries.selectAll().executeAsList())
        }
    }
}
