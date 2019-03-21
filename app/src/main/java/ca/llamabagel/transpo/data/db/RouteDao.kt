/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import androidx.room.*
import ca.llamabagel.transpo.models.app.Route
import ca.llamabagel.transpo.models.app.Stop

@Dao
abstract class RouteDao {

    @Query("SELECT * FROM routes")
    abstract suspend fun getAll(): List<Route>

    @Query("SELECT * FROM routes WHERE id = :id")
    abstract suspend fun getById(id: String): Route?

    @Query("SELECT * FROM routes WHERE shortName = :number")
    abstract suspend fun getByNumber(number: String): Route?

    @Query("SELECT * FROM stops WHERE id IN (SELECT stopId FROM stop_routes WHERE routeId = :routeId)")
    protected abstract suspend fun getStopsByRoute(routeId: String): List<Stop>

    @Query("SELECT * FROM stops WHERE id IN (SELECT stopId FROM stop_routes WHERE routeId = :routeId AND directionId = :directionId)")
    protected abstract suspend fun getStopsByRoute(routeId: String, directionId: Int): List<Stop>

    @Transaction
    open suspend fun getWithStops(routeId: String): RouteWithStops? {
        val route = getById(routeId) ?: return null
        return RouteWithStops(route, getStopsByRoute(routeId))
    }

    @Transaction
    open suspend fun getWithStops(routeId: String, directionId: Int): RouteWithStops? {
        val route = getById(routeId) ?: return null
        return RouteWithStops(route, getStopsByRoute(routeId, directionId))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg route: Route)

    @Delete
    abstract suspend fun delete(vararg route: Route)

}