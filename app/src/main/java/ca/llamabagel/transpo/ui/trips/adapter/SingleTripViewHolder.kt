/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.databinding.TripBinding
import ca.llamabagel.transpo.ui.trips.TripUiModel

class SingleTripViewHolder(private val binding: TripBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trip: TripUiModel) {
        binding.tripUiModel = trip
    }
}