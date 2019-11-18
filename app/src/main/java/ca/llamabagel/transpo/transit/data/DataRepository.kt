/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.transit.data

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.data.api.ApiService
import ca.llamabagel.transpo.data.db.*
import ca.llamabagel.transpo.models.app.AppMetadata
import ca.llamabagel.transpo.models.app.MetadataRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val apiService: ApiService,
    private val database: TransitDatabase,
    private val localMetadataSource: LocalMetadataSource
) {

    suspend fun getRemoteAppMetadata(): AppMetadata = withContext(Dispatchers.IO) {
        val request =
            MetadataRequest(
                localMetadataSource.dataVersion ?: "",
                1, BuildConfig.VERSION_CODE, "android"
            )

        apiService.getMetadata(request)
    }

    suspend fun updateLocalData() = withContext(Dispatchers.IO) {
        val dataPackage = apiService.getDataPackage()

        database.transaction {
            database.stopQueries.deleteAll()
            database.stopQueries.deleteAllFts()
            dataPackage.data.stops.forEach { (id, code, name, latitude, longitude, locationType, parentStation) ->
                database.stopQueries.insert(
                    StopId(id),
                    StopCode(code),
                    name,
                    latitude,
                    longitude,
                    locationType,
                    parentStation
                )
                database.stopQueries.insertfts(
                    id,
                    code,
                    name,
                    latitude,
                    longitude,
                    locationType,
                    parentStation
                )
            }

            database.routeQueries.deleteAll()
            dataPackage.data.routes.forEach { (id, shortName, longName, type, serviceLevel, color) ->
                database.routeQueries.insert(id, shortName, longName, type, serviceLevel, color)
            }

            database.stopRouteQueries.deleteAll()
            dataPackage.data.stopRoutes.forEach { (stopId, routeId, directionId, sequence) ->
                try {
                    database.stopRouteQueries.insert(stopId, routeId, directionId, sequence)
                } catch (e: SQLiteConstraintException) {
                    Log.e(
                        "DataRepository",
                        "Constraint error on: $stopId, $routeId, $directionId, $sequence"
                    )
                }
            }

            database.routeShapeQueries.deleteAll()
            dataPackage.data.shapes.forEach { (routeId, shapeId, data) ->
                try {
                    database.routeShapeQueries.insert(ShapeId(shapeId), RouteId(routeId), data)
                } catch (e: SQLiteConstraintException) {
                    Log.e(
                        "DataRepository",
                        "Constraint error on: $shapeId, $routeId"
                    )
                }
            }
        }

        localMetadataSource.dataVersion = dataPackage.dataVersion
    }

    suspend fun getLocalAppMetadata(): AppMetadata = withContext(Dispatchers.IO) {
        AppMetadata(localMetadataSource.dataVersion ?: "", 1, "${BuildConfig.VERSION_CODE}")
    }
}