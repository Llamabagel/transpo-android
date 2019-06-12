/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.databinding.TripBinding
import ca.llamabagel.transpo.ui.trips.TripUiModel

class SingleTripViewHolder(
    private val binding: TripBinding,
    private val itemClickListener: (TripAdapterItem) -> Unit,
    private val itemSelectionListener: (TripAdapterItem, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var tripItem: TripItem

    internal inner class Handler {
        fun onClick() {
            itemClickListener(tripItem)
        }

        fun onLongClick() {
            itemSelectionListener(tripItem, !tripItem.selected)
        }
    }

    fun bind(trip: TripItem) {
        tripItem = trip
        binding.tripUiModel = trip
        binding.handler = Handler()
    }
}