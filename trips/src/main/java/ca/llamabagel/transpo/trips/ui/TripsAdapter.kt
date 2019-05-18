/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.trips.R
import ca.llamabagel.transpo.trips.databinding.RouteBinding

class TripsAdapter(private val apiData: ApiResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RouteBinding>(layoutInflater, R.layout.route, parent, false)
        return RouteHolder(binding)
    }

    override fun getItemCount(): Int = apiData.routes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RouteHolder).bind(apiData.routes[position])
    }

    class RouteHolder(private val binding: RouteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(route: Route) {
            binding.route = route
        }

    }
}