package ca.llamabagel.transpo.trips.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.trips.domain.GetNextBusTripsUseCase
import ca.llamabagel.transpo.trips.ui.adapter.TripAdapterItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class StopViewModel @Inject constructor(
    private val getNextBusTrips: GetNextBusTripsUseCase
) : ViewModel() {

    private var stopId: StopId = StopId.DEFAULT

    private val _resultsData = MutableLiveData<List<TripAdapterItem>>()
    val resultsData: LiveData<List<TripAdapterItem>> = _resultsData

    fun setStop(stopId: StopId) {
        this.stopId = stopId

        viewModelScope.launch {
            getNextBusTrips(stopId).collect { data -> _resultsData.value = data }
        }
    }
}