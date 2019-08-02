package ca.llamabagel.transpo.map.domain

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.map.data.MapRepository
import ca.llamabagel.transpo.map.data.MapRepository.Companion.STOPS_SOURCE_ID
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStopsDataSourceUseCase @Inject constructor(
    private val repository: MapRepository,
    private val dispatchers: CoroutinesDispatcherProvider,
    private val gson: Gson
) {
    suspend operator fun invoke(): Flow<GeoJsonSource> = repository.getAllStops()
        .map { stops -> stops.asFeatures() }
        .map { features -> FeatureCollection.fromFeatures(features) }
        .map { collection -> GeoJsonSource(STOPS_SOURCE_ID, collection) }
        .flowOn(dispatchers.main)

    private fun List<Stop>.asFeatures(): List<Feature> {
        return map { stop ->
            val point = Point.fromLngLat(stop.longitude, stop.latitude)
            val props = JsonObject().apply {
                addProperty("id", stop.id.value)
                addProperty("code", stop.code.value)
                addProperty("name", stop.name)
            }

            Feature.fromGeometry(point, props)
        }
    }
}