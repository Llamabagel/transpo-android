/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.data.api.DataService
import ca.llamabagel.transpo.data.db.TransitDatabase
import ca.llamabagel.transpo.models.app.AppMetadata
import ca.llamabagel.transpo.models.app.MetadataRequest
import ca.llamabagel.transpo.models.app.Version
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val dataService: DataService,
    private val database: TransitDatabase,
    private val localMetadataSource: LocalMetadataSource
) {

    suspend fun getRemoteAppMetadata(): AppMetadata = withContext(Dispatchers.IO) {
        val request =
            MetadataRequest(
                Version(localMetadataSource.dataVersion ?: ""),
                1, BuildConfig.VERSION_CODE, "android"
            )

        dataService.getMetadata(request)
    }

    suspend fun updateLocalData() = withContext(Dispatchers.IO) {
        val dataPackage = dataService.getDataPackage()

        database.stopQueries.deleteAll()
        dataPackage.data.stops.forEach { (id, code, name, latitude, longitude, locationType, parentStation) ->
            database.stopQueries.insert(id, code, name, latitude, longitude, locationType, parentStation)
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

}