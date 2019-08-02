/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.map.domain.GetStopDetailUseCase
import ca.llamabagel.transpo.map.domain.GetStopsDataSourceUseCase
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val getStopsDataSource: GetStopsDataSourceUseCase,
    private val getStopDetail: GetStopDetailUseCase
) : ViewModel() {

    private val _stopsSource = MutableLiveData<GeoJsonSource>()
    val stopsSource: LiveData<GeoJsonSource> = _stopsSource

    private val _stopDetail = MutableLiveData<Stop?>()
    val stopDetail: LiveData<Stop?> = _stopDetail

    init {
        viewModelScope.launch {
            getStopsDataSource().collect { source ->
                _stopsSource.value = source
            }
        }
    }

    fun openStopDetails(id: String?) = viewModelScope.launch {
        if (id == null) {
            _stopDetail.value = null
        } else {
            _stopDetail.value = getStopDetail(StopId(id))
        }
    }
}
