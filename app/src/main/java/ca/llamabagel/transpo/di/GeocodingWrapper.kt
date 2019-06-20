/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import androidx.annotation.FloatRange
import com.mapbox.api.geocoding.v5.models.CarmenFeature

interface GeocodingWrapper {
    @Suppress("MagicNumber")
    fun getAutocompleteResults(
        query: String,
        @FloatRange(from = -180.0, to = 180.0) minLongitude: Double = -180.0,
        @FloatRange(from = -90.0, to = 90.0) minLatitude: Double = -90.0,
        @FloatRange(from = -180.0, to = 180.0) maxLongitude: Double = 180.0,
        @FloatRange(from = -90.0, to = 90.0) maxLatitude: Double = 90.0,
        limit: Int = 10
    ): List<CarmenFeature>
}