/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import androidx.room.*
import ca.llamabagel.transpo.models.app.Stop

@Dao
interface StopDao {

    @Query("SELECT * FROM stops")
    suspend fun getAll(): List<Stop>

    @Query("SELECT * FROM stops WHERE id = :id")
    suspend fun getById(id: String): Stop?

    @Query("SELECT * FROM stops WHERE code = :code")
    suspend fun getByCode(code: String): List<Stop>

    @Query("SELECT * FROM stops WHERE id IN (SELECT stopId FROM stop_routes WHERE routeId = :routeId)")
    suspend fun getByRoute(routeId: String): List<Stop>

    @Query("SELECT * FROM stops WHERE id IN (SELECT stopId FROM stop_routes WHERE routeId = :routeId AND directionId = :directionId)")
    suspend fun getByRoute(routeId: String, directionId: Int): List<Stop>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg stop: Stop)

    @Delete
    suspend fun delete(vararg stop: Stop)

}