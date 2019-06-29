/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.models.trips.Trip

class TripDetailsFragment : Fragment() {

    private val activityViewModel: TripsViewModel by activityViewModels()
    private val viewModel: TripDetailsViewModel by viewModels { injector.tripDetailsViewModelFactory() }

    private val args: TripDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.trip_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setStop(StopId(args.stopId), args.trip)
        viewModel.tripData.observe(this, Observer(::displayTrip))
    }

    private fun displayTrip(trip: Trip) {
        view?.run {
            findViewById<TextView>(R.id.heading).text = trip.destination
            findViewById<TextView>(R.id.has_bike).text = "Has Bike Rack? ${trip.hasBikeRack}"
            findViewById<TextView>(R.id.bus_type).text = "Bus type: ${trip.busType}"
        }
    }

    companion object {
        fun newInstance() = TripDetailsFragment()
    }
}
