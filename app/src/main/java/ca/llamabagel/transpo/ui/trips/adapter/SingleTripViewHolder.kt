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

    private lateinit var tripUiModel: TripUiModel

    internal inner class Handler {
        fun onClick() {
            itemClickListener(TripItem(tripUiModel))
        }

        fun onLongClick() {
            itemSelectionListener(TripItem(tripUiModel), !tripUiModel.selected)
        }
    }

    fun bind(trip: TripUiModel) {
        tripUiModel = trip
        binding.tripUiModel = trip
        binding.handler = Handler()
    }
}