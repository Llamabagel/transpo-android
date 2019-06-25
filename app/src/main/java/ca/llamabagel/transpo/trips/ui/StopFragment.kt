/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.trips.ui.adapter.TripAdapterItem
import ca.llamabagel.transpo.trips.ui.adapter.TripItem
import ca.llamabagel.transpo.trips.ui.adapter.TripsAdapter

class StopFragment : Fragment() {

    private val activityViewModel: TripsViewModel by activityViewModels()
    private val viewModel: StopViewModel by viewModels { injector.stopViewModelFactory() }

    private val args: StopFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.stop_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripsAdapter(itemClickListener = ::itemSelected)
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        activityViewModel.isRefreshing.observe(this, Observer { isRefreshing ->
            view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).isRefreshing = isRefreshing
        })

        viewModel.setStop(StopId(args.stopId))
        viewModel.resultsData.observe(this, Observer(adapter::submitList))

        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).setOnRefreshListener {
            activityViewModel.getTrips()
        }
    }

    private fun itemSelected(item: TripAdapterItem) {
        when (item) {
            is TripItem -> {
                val action =
                    StopFragmentDirections.actionStopFragmentToTripsFragment(
                        arrayOf(RouteSelection(item.route.number, item.route.directionId)),
                        args.stopId
                    )
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        fun newInstance() = StopFragment()
    }
}
