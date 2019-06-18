/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.models.trips.Trip

sealed class TripAdapterItem {
    abstract infix fun sameAs(other: TripAdapterItem): Boolean

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TripAdapterItem>() {

            override fun areItemsTheSame(oldItem: TripAdapterItem, newItem: TripAdapterItem): Boolean =
                oldItem sameAs newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: TripAdapterItem, newItem: TripAdapterItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class TripItem(val route: Route, val trip: Trip, val selected: Boolean = false) : TripAdapterItem() {
    val adjustedScheduleTimeString: String get() = trip.adjustedScheduleTime.toString()

    override infix fun sameAs(other: TripAdapterItem): Boolean {
        if (other !is TripItem) return false

        return other.route.number == route.number &&
                other.route.directionId == route.directionId &&
                other.trip.startTime == trip.startTime
    }
}
