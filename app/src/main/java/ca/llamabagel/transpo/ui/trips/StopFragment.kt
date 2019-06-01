/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.ui.trips.adapter.TripsAdapter

class StopFragment : Fragment() {

    private val viewModel: TripsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.stop_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.displayData.observe(this, Observer { data ->
            data ?: return@Observer

            view.findViewById<RecyclerView>(R.id.recycler_view).adapter = TripsAdapter(data,
                itemClickListener = { item ->
                    when (item) {
                        is TripItem -> {
                            viewModel.updateRouteSelection(
                                item.tripUiModel.route.number,
                                item.tripUiModel.route.directionId,
                                true
                            )
                            findNavController().navigate(R.id.tripsFragment)
                        }
                    }
                })
        })
    }

    companion object {
        fun newInstance() = StopFragment()
    }
}
