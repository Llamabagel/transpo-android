/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.TripBinding
import ca.llamabagel.transpo.ui.trips.TripAdapterItem
import ca.llamabagel.transpo.ui.trips.TripItem

class TripsAdapter(
    private val items: List<TripAdapterItem>,
    private val itemClickListener: (TripAdapterItem) -> Unit = {},
    private val itemSelectionListener: (TripAdapterItem, Boolean) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.trip -> {
                val binding = DataBindingUtil.inflate<TripBinding>(layoutInflater, R.layout.trip, parent, false)
                SingleTripViewHolder(binding, itemClickListener, itemSelectionListener)
            }
            else -> throw IllegalArgumentException("No ViewHolder for type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is TripItem -> R.layout.trip
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SingleTripViewHolder -> holder.bind((items[position] as TripItem).tripUiModel)
        }
    }
}