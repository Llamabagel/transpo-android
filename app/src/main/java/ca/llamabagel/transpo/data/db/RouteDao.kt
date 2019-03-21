/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import androidx.room.*
import ca.llamabagel.transpo.models.app.Route

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes")
    suspend fun getAll(): List<Route>

    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getById(id: String): Route?

    @Query("SELECT * FROM routes WHERE shortName = :number")
    suspend fun getByNumber(number: String): Route?

    @Query("SELECT * FROM routes WHERE id IN (SELECT stopId FROM stop_routes WHERE stopId = :stopId)")
    suspend fun getByStopId(stopId: String): List<Route>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg route: Route)

    @Delete
    suspend fun delete(vararg route: Route)

}