/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.di.GeocodingWrapper
import com.mapbox.api.geocoding.v5.models.CarmenFeature

class FakeGeocoder : GeocodingWrapper {
    override fun getAutocompleteResults(
        query: String,
        minLongitude: Double,
        minLatitude: Double,
        maxLongitude: Double,
        maxLatitude: Double
    ): List<CarmenFeature> {
        return when (query) {
            "Parliament" -> listOf(TestPlace.parliament)
            "2" -> listOf(TestPlace.lisgar29)
            else -> emptyList()
        }
    }
}

object TestPlace {
    val parliament =
        CarmenFeature.fromJson("""{"type":"Feature","bbox":[-75.7224416,45.4192953,-75.6984858,45.4371135],"id":"neighborhood.8825866183174760","geometry":{"coordinates":[-75.7001,45.4249],"type":"Point"},"text":"Parliamentary Precinct","place_name":"Parliamentary Precinct, K1A 1M1, Ottawa, Ontario, Canada","place_type":["neighborhood"],"center":[-75.7001,45.4249],"context":[{"id":"postcode.11399684863661720","text":"K1A 1M1"},{"id":"place.15087147145789660","text":"Ottawa","wikidata":"Q1930"},{"id":"region.7377835739263190","text":"Ontario","short_code":"CA-ON","wikidata":"Q1904"},{"id":"country.4282270149587150","text":"Canada","short_code":"ca","wikidata":"Q16"}],"relevance":1.0}""")

    var lisgar29 =
        CarmenFeature.fromJson("""{"type":"Feature","id":"address.5714516353518348","geometry":{"coordinates":[-75.688001,45.420854],"type":"Point"},"properties":{"accuracy":"point"},"text":"Lisgar Street","place_name":"29 Lisgar Street, Ottawa, Ontario K2P 0C1, Canada","place_type":["address"],"address":"29","center":[-75.688001,45.420854],"context":[{"id":"neighborhood.15312013322120830","text":"The Canal"},{"id":"postcode.13752684191404510","text":"K2P 0C1"},{"id":"place.15087147145789660","text":"Ottawa","wikidata":"Q1930"},{"id":"region.7377835739263190","text":"Ontario","short_code":"CA-ON","wikidata":"Q1904"},{"id":"country.4282270149587150","text":"Canada","short_code":"ca","wikidata":"Q16"}],"relevance":1.0}""")
}