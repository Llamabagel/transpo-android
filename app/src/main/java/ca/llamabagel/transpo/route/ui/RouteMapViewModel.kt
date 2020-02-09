package ca.llamabagel.transpo.route.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.RouteId
import ca.llamabagel.transpo.route.domain.GetRouteFeaturesUseCase
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.launch
import javax.inject.Inject

class RouteMapViewModel @Inject constructor(private val getRouteFeatures: GetRouteFeaturesUseCase) : ViewModel() {
    private val _routeSource = MutableLiveData<Pair<LatLngBounds, GeoJsonSource>>()
    val routeSource: LiveData<Pair<LatLngBounds, GeoJsonSource>> = _routeSource

    fun loadRoute(routeId: String) = viewModelScope.launch {
        _routeSource.postValue(getRouteFeatures(RouteId(routeId)))
    }
}