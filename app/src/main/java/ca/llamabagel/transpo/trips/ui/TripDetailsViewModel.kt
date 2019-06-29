package ca.llamabagel.transpo.trips.ui

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.models.trips.Trip
import ca.llamabagel.transpo.trips.domain.GetTripDetailsUseCase
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripDetailsViewModel @Inject constructor(
    private val getTripDetails: GetTripDetailsUseCase
) : ViewModel() {

    private val _tripData = MutableLiveData<Trip>()
    val tripData: LiveData<Trip> = _tripData

    fun setStop(stopId: StopId, trip: SingleTrip) = viewModelScope.launch {
        getTripDetails(stopId, trip).collect { item ->
            if (item != null) {
                _tripData.value = item
            }
        }
    }
}

@Parcelize
data class SingleTrip(val routeNumber: String, val directionId: Int, val startTime: String) : Parcelable