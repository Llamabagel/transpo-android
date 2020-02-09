package ca.llamabagel.transpo.route.domain

import ca.llamabagel.transpo.data.CoroutinesDispatcherProvider
import ca.llamabagel.transpo.data.db.RouteId
import ca.llamabagel.transpo.route.data.RouteRepository
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRouteFeaturesUseCase @Inject constructor(
    private val repository: RouteRepository,
    private val dispatchers: CoroutinesDispatcherProvider
) {
    suspend operator fun invoke(routeId: RouteId): Pair<LatLngBounds, GeoJsonSource> =
        withContext(dispatchers.computation) {
            val shapes = repository.getShapesForRoute(routeId)
            val bounds = LatLngBounds.Builder()

            val features = shapes.map { shape -> PolylineUtils.decode(shape.shape_data, 5) }
                .map {
                    bounds.includes(it.map { point -> LatLng(point.latitude(), point.longitude()) })
                    LineString.fromLngLats(it)
                }
                .map { Feature.fromGeometry(it) }

            val collection = FeatureCollection.fromFeatures(features)
            withContext(dispatchers.main) {
                bounds.build() to GeoJsonSource(routeId.value, collection)
            }
        }
}