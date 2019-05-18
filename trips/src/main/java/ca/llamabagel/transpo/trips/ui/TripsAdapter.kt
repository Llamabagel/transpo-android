/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.models.trips.ApiResponse
import ca.llamabagel.transpo.models.trips.Route
import ca.llamabagel.transpo.trips.R

class TripsAdapter(private val apiData: ApiResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RouteHolder(LayoutInflater.from(parent.context).inflate(R.layout.route, parent, false))
    }

    override fun getItemCount(): Int = apiData.routes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RouteHolder).bind(apiData.routes[position])
    }

    class RouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView: TextView by lazy { itemView.findViewById<TextView>(R.id.textView) }

        @SuppressLint("SetTextI18n")
        fun bind(route: Route) {
            textView.text = "${route.number} ${route.heading}"
        }

    }
}