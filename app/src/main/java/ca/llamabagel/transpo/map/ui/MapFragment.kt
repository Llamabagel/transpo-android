/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.map.ui

import android.content.res.Configuration
import android.graphics.RectF
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ca.llamabagel.transpo.BuildConfig
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.di.injector
import ca.llamabagel.transpo.map.data.MapRepository.Companion.STOPS_SOURCE_ID
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius

class MapFragment : Fragment() {

    companion object {
        fun newInstance() = MapFragment()
    }

    private val viewModel: MapViewModel by viewModels { requireActivity().injector.mapViewModelFactory() }
    private var map: MapboxMap? = null

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
        mapView.getMapAsync { mapboxMap ->
            val mapStyle = when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> Style.TRAFFIC_NIGHT
                else -> Style.TRAFFIC_DAY
            }

            mapboxMap.setStyle(mapStyle, ::prepareMap)

            mapboxMap.addOnMapClickListener { point -> handleMapClick(mapboxMap, point) }

            map = mapboxMap
        }

        val bottomSheet = view.findViewById<FrameLayout>(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.stopDetail.observe(this, Observer { stop ->
            stop ?: return@Observer

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            view.findViewById<TextView>(R.id.title).text = stop.name
            view.findViewById<TextView>(R.id.subtitle).text = stop.code.value

            view.findViewById<MaterialCardView>(R.id.details_card).setOnClickListener {
                findNavController().navigate(MapFragmentDirections.actionGlobalTripsActivity(stop.id.value))
            }
        })
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
    private fun prepareMap(style: Style) {
        viewModel.stopsSource.observe(this, Observer { source ->
            if (style.getSource(STOPS_SOURCE_ID) != null) {
                style.removeSource(STOPS_SOURCE_ID)
            }

            if (style.getLayer("stops-layer") == null) {
                val circleLayer = CircleLayer("stops-layer", STOPS_SOURCE_ID)

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

                style.addLayer(circleLayer)
            }

            style.addSource(source)
        })
    }

    private fun handleMapClick(map: MapboxMap, point: LatLng): Boolean {
        val pointF = map.projection.toScreenLocation(point)
        val clickBuffer = 10
        val rectF =
            RectF(pointF.x - clickBuffer, pointF.y - clickBuffer, pointF.x + clickBuffer, pointF.y - clickBuffer)
        val features = map.queryRenderedFeatures(rectF, "stops-layer")

        if (features.isNotEmpty()) {
            viewModel.openStopDetails(features[0].getStringProperty("id"))
            view?.findViewById<View>(R.id.map_view)?.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            return true
        }

        return false
    }
}
