/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.data.db

import androidx.room.Dao
import androidx.room.Query
import ca.llamabagel.transpo.models.transit.RouteShape

@Dao
interface RouteShapeDao {

    @Query("SELECT * FROM route_shapes WHERE routeId = :routeId")
    suspend fun getByRouteId(routeId: String): List<RouteShape>

    @Query("SELECT * FROM route_shapes WHERE shapeId = :shapeId")
    suspend fun getByShapeId(shapeId: String): RouteShape?
}