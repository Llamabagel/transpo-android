/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.transit.data

import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.LocalMetadataSource
import ca.llamabagel.transpo.data.api.ApiService
import ca.llamabagel.transpo.data.db.StopCode
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
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
                database.stopQueries.insertfts(id, code, name, latitude, longitude, locationType, parentStation)
            }

            database.routeQueries.deleteAll()
            dataPackage.data.routes.forEach { (id, shortName, longName, type, serviceLevel, color) ->
                database.routeQueries.insert(id, shortName, longName, type, serviceLevel, color)
            }

            database.stopRouteQueries.deleteAll()
            dataPackage.data.stopRoutes.forEach { (stopId, routeId, directionId, sequence) ->
                database.stopRouteQueries.insert(stopId, routeId, directionId, sequence)
            }
        }

        localMetadataSource.dataVersion = dataPackage.dataVersion
    }

    suspend fun getLocalAppMetadata(): AppMetadata = withContext(Dispatchers.IO) {
        AppMetadata(localMetadataSource.dataVersion ?: "", 1, "${BuildConfig.VERSION_CODE}")
    }
}