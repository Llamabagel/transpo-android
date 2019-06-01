/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.ui.trips.TripAdapterItem

class RouteViewHolder(private val binding: ViewDataBinding,
                      private val itemClickListener: (TripAdapterItem) -> Unit,
                      private val itemSelectionListener: (TripAdapterItem, Boolean) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun bind(route: Route) {

    }
}