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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.ui.trips.adapter.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.adapter.TripItem
import ca.llamabagel.transpo.ui.trips.adapter.TripsAdapter

class StopFragment : Fragment() {

    private val viewModel: TripsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.stop_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripsAdapter(itemClickListener = ::itemSelected)
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        viewModel.isRefreshing.observe(this, Observer { isRefreshing ->
            view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).isRefreshing = isRefreshing
        })

        viewModel.displayData.observe(this, Observer(adapter::submitList))

        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).setOnRefreshListener {
            viewModel.getTrips()
        }
    }

    private fun itemSelected(item: TripAdapterItem) {
        when (item) {
            is TripItem -> {
                val action = StopFragmentDirections.actionStopFragmentToTripsFragment(arrayOf(
                    RouteSelection(item.route.number, item.route.directionId)
                ))
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        fun newInstance() = StopFragment()
    }
}
