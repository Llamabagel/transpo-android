/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.trips.ui

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.trips.ui.adapter.TripItem
import ca.llamabagel.transpo.trips.ui.adapter.TripsAdapter
import com.mapbox.geojson.Feature
import com.mapbox.geojson.MultiPoint
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class TripsFragment : Fragment() {

    private val activityViewModel: TripsViewModel by activityViewModels()
    private val viewModel: TripsMapViewModel by viewModels { injector.tripsMapViewModelFactory() }

    private val args: TripsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireActivity(), BuildConfig.MAPBOX_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.trips_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripsAdapter(itemClickListener = {
            if (it is TripItem) {
                val action = TripsFragmentDirections.actionTripsFragmentToTripDetailsFragment(
                    args.stopId,
                    SingleTrip(it.route.number, it.route.directionId, it.trip.startTime)
                )
                findNavController().navigate(action)
            }
        })
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter

        viewModel.setStop(StopId(args.stopId), args.selectedRoutes)
        viewModel.viewerData.observe(this, Observer(adapter::submitList))

        val mapView = view.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            val mapStyle = when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> Style.TRAFFIC_NIGHT
                else -> Style.TRAFFIC_DAY
            }

            map.setStyle(mapStyle) {
                prepareMap(map)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<MapView>(R.id.map_view)?.onStart()
    }

    override fun onPause() {
        super.onPause()
        view?.findViewById<MapView>(R.id.map_view)?.onPause()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<MapView>(R.id.map_view)?.onResume()
    }

    override fun onStop() {
        super.onStop()
        view?.findViewById<MapView>(R.id.map_view)?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.findViewById<MapView>(R.id.map_view)?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view?.findViewById<MapView>(R.id.map_view)?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view?.findViewById<MapView>(R.id.map_view)?.onSaveInstanceState(outState)
    }

    @Suppress("MagicNumber")
    private fun prepareMap(map: MapboxMap) {
        // TODO: Move this into a domain layer or something like damn
        viewModel.viewerData.observe(this, Observer { items ->
            map.style?.addImage(
                "marker-icon-id",
                BitmapFactory.decodeResource(requireActivity().resources, R.drawable.mapbox_marker_icon_default)
            )

            val points = items.asSequence()
                .mapNotNull { item -> item as? TripItem }
                .filter { (_, trip) -> trip.adjustmentAge > 0 }
                .map { (_, trip) -> Point.fromLngLat(trip.longitude!!, trip.latitude!!) }
                .toList()

            val geometry = Feature.fromGeometry(MultiPoint.fromLngLats(points))
            if (points.size > 1) {
                val lngLatBounds =
                    LatLngBounds.Builder().includes(points.map { LatLng(it.latitude(), it.longitude()) }).build()
                // TODO: Replace with actual measurement, once we determine what that will be
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(lngLatBounds, 32))
            } else if (points.isNotEmpty()) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(points[0].latitude(), points[0].longitude()),
                        15.0 // TODO: Replace with actual measurement, once we determine what that will be
                    )
                )
            }
            val source = GeoJsonSource("trip-id", geometry)
            map.style?.addSource(source)

            val symbolLayer = SymbolLayer("trips-layer", "trip-id").apply {
                withProperties(PropertyFactory.iconImage("marker-icon-id"))
            }
            map.style?.addLayer(symbolLayer)
        })
    }

    companion object {
        fun newInstance() = TripsFragment()
    }
}
