package ca.llamabagel.transpo.route.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.PropertyFactory


class RouteMapFragment : Fragment(R.layout.route_map_fragment) {
    private val viewModel: RouteMapViewModel by viewModels { injector.routeMapViewModelFactory() }
    private lateinit var map: MapboxMap
    private val routeId: String by lazy { arguments?.getString(ROUTE_ID_KEY)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireActivity(), BuildConfig.MAPBOX_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { loadedMap ->
            map = loadedMap
            map.setStyle(Style.LIGHT) {
                viewModel.loadRoute(routeId)
            }
        }

        viewModel.routeSource.observe(this, Observer {
            if (map.style?.getSource(routeId) != null) {
                map.style?.removeLayer(routeId)
                map.style?.removeSource(routeId)
            }

            map.style?.addSource(it)

            val layer = LineLayer(routeId, routeId).apply {
                withProperties(
                    PropertyFactory.lineWidth(5f)
                )
            }
            map.style?.addLayer(layer)
        })
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

    companion object {
        private const val ROUTE_ID_KEY = "route_id"

        fun newInstance(routeId: String): RouteMapFragment {
            val args = Bundle()
            args.putString(ROUTE_ID_KEY, routeId)

            val fragment = RouteMapFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
