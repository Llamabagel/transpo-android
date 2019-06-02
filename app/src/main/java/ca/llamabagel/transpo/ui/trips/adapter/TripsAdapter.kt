/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.TripBinding

class TripsAdapter(
    private val itemClickListener: (TripAdapterItem) -> Unit = {},
    private val itemSelectionListener: (TripAdapterItem, Boolean) -> Unit = { _, _ -> }
) : ListAdapter<TripAdapterItem, RecyclerView.ViewHolder>(TripAdapterItem.DIFF_CALLBACK) {

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

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TripItem -> R.layout.trip
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SingleTripViewHolder -> holder.bind((getItem(position) as TripItem).tripUiModel)
        }
    }
}