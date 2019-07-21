package ca.llamabagel.transpo.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.LiveUpdateBinding
import ca.llamabagel.transpo.models.updates.LiveUpdate

class LiveUpdatesAdapter : ListAdapter<LiveUpdate, LiveUpdatesAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(private val binding: LiveUpdateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(update: LiveUpdate) {
            binding.update = update
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LiveUpdateBinding>(layoutInflater, R.layout.live_update, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val DiffCallback = object : DiffUtil.ItemCallback<LiveUpdate>() {
    override fun areItemsTheSame(oldItem: LiveUpdate, newItem: LiveUpdate): Boolean = oldItem.guid == newItem.guid

    override fun areContentsTheSame(oldItem: LiveUpdate, newItem: LiveUpdate): Boolean = oldItem == newItem
}
