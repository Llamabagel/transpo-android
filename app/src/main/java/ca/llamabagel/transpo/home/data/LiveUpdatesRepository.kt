package ca.llamabagel.transpo.home.data

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.Result
import ca.llamabagel.transpo.data.api.ApiService
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.updates.AffectedStop
import ca.llamabagel.transpo.models.updates.LiveUpdate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveUpdatesRepository @Inject constructor(
    private val apiService: ApiService,
    private val database: TransitDatabase,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    private val cache by lazy {
        database.liveUpdateQueries.get { guid, _, title, date, category, link, description, featuredImageUrl ->
            val routes = database.liveUpdateQueries.getRoutes(guid) { _, routeNumber -> routeNumber }.executeAsList()
            val stops = database.liveUpdateQueries.getStops(guid) { _, stopCode, alternateStop ->
                AffectedStop(
                    stopCode,
                    alternateStop
                )
            }.executeAsList()

            LiveUpdate(title, date, category, link, description, guid, routes, stops, featuredImageUrl)
        }
            .asFlow()
            .flowOn(dispatchers.io)
            .mapToList()
    }

    suspend fun updateLiveUpdates(): Result<Unit> = withContext(dispatchers.io) {
        val response: Result<List<LiveUpdate>> = try {
            Result.Success(apiService.getLiveUpdates())
        } catch (e: Exception) {
            Result.Error(e)
        }

        // TODO: Handle french
        when (response) {
            is Result.Success<List<LiveUpdate>> -> saveResponse(response)
            else -> {
            }
        }

        return@withContext when (response) {
            is Result.Success<*> -> Result.Success<Unit>(Unit)
            is Result.Loading<*> -> Result.Loading<Unit>()
            is Result.Error<*> -> Result.Error<Unit>(response.exception)
        }
    }

    fun getLiveUpdatesFlow(): Flow<List<LiveUpdate>> = cache

    private fun saveResponse(response: Result.Success<List<LiveUpdate>>) = database.transaction {
        database.liveUpdateQueries.clear(response.data.map { it.guid })
        response.data.forEach { (title, date, category, link, description,
                                    guid, affectedRoutes, affectedStops, featuredImageUrl) ->
            database.liveUpdateQueries.insertLiveUpdate(
                guid,
                "en",
                title,
                date,
                category,
                link,
                description,
                featuredImageUrl
            )

            affectedRoutes.forEach { route ->
                database.liveUpdateQueries.insertRoute(guid, route)
            }

            affectedStops.forEach { (stop, alternate) ->
                database.liveUpdateQueries.insertStop(guid, stop, alternate)
            }
        }
    }
}
