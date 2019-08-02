package ca.llamabagel.transpo.map.data

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val database: TransitDatabase,
    private val dispatchers: CoroutinesDispatcherProvider
) {

    suspend fun getAllStops(): Flow<List<Stop>> = withContext(dispatchers.io) {
        database.stopQueries.getAll().asFlow().mapToList()
    }

    suspend fun getStop(id: StopId): Stop? = withContext(dispatchers.io) {
        database.stopQueries.getStopById(id).executeAsOneOrNull()
    }

    companion object {
        const val STOPS_SOURCE_ID = "stops"
    }

}