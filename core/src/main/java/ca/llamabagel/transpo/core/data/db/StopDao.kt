/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.data.db

import androidx.room.*
import ca.llamabagel.transpo.models.transit.Route
import ca.llamabagel.transpo.models.transit.Stop

@Dao
abstract class StopDao {

    @Query("SELECT * FROM stops")
    abstract suspend fun getAll(): List<Stop>

    @Query("SELECT * FROM stops WHERE id = :id")
    abstract suspend fun getById(id: String): Stop?

    @Query("SELECT * FROM stops WHERE code = :code")
    abstract suspend fun getByCode(code: String): List<Stop>

    @Transaction
    open suspend fun getWithRoutesById(id: String): StopWithRoutes? {
        val stop = getById(id) ?: return null
        return StopWithRoutes(stop, getRoutes(id))
    }

    @Query("SELECT * FROM routes WHERE id IN (SELECT stopId FROM stop_routes WHERE stopId = :stopId)")
    protected abstract suspend fun getRoutes(stopId: String): List<Route>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(vararg stop: Stop)

    @Delete
    abstract suspend fun delete(vararg stop: Stop)

}