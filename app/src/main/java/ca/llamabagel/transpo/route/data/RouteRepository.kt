package ca.llamabagel.transpo.route.data

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.RouteId
import ca.llamabagel.transpo.data.db.Route_shape
import ca.llamabagel.transpo.data.db.TransitDatabase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val database: TransitDatabase,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend fun getShapesForRoute(routeId: RouteId): List<Route_shape> =
        withContext(dispatchers.io) {
            database.routeShapeQueries.getByRouteId(routeId).executeAsList()
        }
}