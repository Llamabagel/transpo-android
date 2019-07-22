package ca.llamabagel.transpo.settings.data

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Settings {
    val useCellularForDataDownloads: Setting<Boolean>
    val groupByRoute: Setting<Boolean>
}

class Setting<T>(
    val get: () -> T,
    val set: (T) -> Unit
) {
    var value: T
        get() = get()
        set(value) {
            set(value)
            channel.offer(value)
        }

    private val channel: ConflatedBroadcastChannel<T> = ConflatedBroadcastChannel()
    val flow: Flow<T> get() = channel.asFlow()
}