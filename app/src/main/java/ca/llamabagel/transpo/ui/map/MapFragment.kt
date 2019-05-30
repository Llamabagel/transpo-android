/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.interpolate
import com.mapbox.mapboxsdk.style.expressions.Expression.exponential
import com.mapbox.mapboxsdk.style.expressions.Expression.zoom
import com.mapbox.mapboxsdk.style.expressions.Expression.stop
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels { requireActivity().injector.mapViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(requireActivity(), BuildConfig.MAPBOX_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            map.setStyle(Style.TRAFFIC_DAY) {
                prepareMap(map)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<MapView>(R.id.map_view)?.onStart()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<MapView>(R.id.map_view)?.onResume()
    }

    override fun onStop() {
        super.onStop()
        view?.findViewById<MapView>(R.id.map_view)?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        view?.findViewById<MapView>(R.id.map_view)?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view?.findViewById<MapView>(R.id.map_view)?.onLowMemory()
    }

    private fun prepareMap(map: MapboxMap) {
        viewModel.getStops()
        viewModel.stops.observe(this, Observer { stops ->

            val features = stops.map { Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude)) }
            val source = GeoJsonSource("stops", FeatureCollection.fromFeatures(features))

            val circleLayer = CircleLayer("stops-layer", "stops")

            circleLayer.withProperties(
                circleRadius(
                    interpolate(
                        exponential(1.75f),
                        zoom(),
                        stop(12, 2f),
                        stop(22, 180f)
                    )
                ),
                circleColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            )

            map.style?.addSource(source)
            map.style?.addLayer(circleLayer)
        })
    }
}
