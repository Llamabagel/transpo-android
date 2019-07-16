/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.db.*
import ca.llamabagel.transpo.di.TransitDatabaseModule.Companion.RECENT_ADAPTER
import ca.llamabagel.transpo.di.TransitDatabaseModule.Companion.STOP_ADAPTER
import ca.llamabagel.transpo.search.data.SearchFilters
import ca.llamabagel.transpo.search.ui.viewholders.RecentResult
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

fun getTransitDatabase(): TransitDatabase {
    val driver = JdbcSqliteDriver()
    TransitDatabase.Schema.create(driver)

    val database = TransitDatabase(driver, RECENT_ADAPTER, STOP_ADAPTER)
    populateTestData(database)

    return database
}

object TestStops {
    val mackenzieKing1A =
        Stop.Impl(StopId("CD900"), StopCode("3000"), "Mackenzie King 1A", 45.42414, -75.6893711, 0, "MAC")
    val mackenzieKing2A =
        Stop.Impl(StopId("CD910"), StopCode("3000"), "Mackenzie King 2A", 45.424126, -75.689985, 0, "MAC")
    val mackenzieKing =
        Stop.Impl(StopId("MAC"), StopCode("3000"), "Mackenzie King Station", 45.424237, -75.689459, 1, null)

    val walkleyJasper = Stop.Impl(StopId("AH060"), StopCode("7196"), "Walkley / Jasper", 45.373063, -75.656894, 0, null)
    val lincolnFields = Stop.Impl(StopId("JS034"), StopCode("3014"), "Lincoln Fields", 45.234235, -75.543534, 0, null)
}

object TestRoutes {
    val route2 = Route.Impl("2-288", "2", "", 2, "", "")
    val route44 = Route.Impl("44-288", "44", "", 3, "", "")
}

object TestRecent {
    val mackenzieKing = Recent_search.Impl("MAC", SearchFilters.STOP, 1, "Mackenzie", "No Upcoming Trips", null, "3000")
    val route95 = Recent_search.Impl("1234", SearchFilters.ROUTE, 1, "Name", "conexion", "95", null)
    val laurier110 = Recent_search.Impl("address-3453", SearchFilters.PLACE, 1, "110 Laurier", "Ottawa", null, null)
}

fun Recent_search.Impl.toSearchResult() = RecentResult(primary_text, secondary_text, number, code, type, id)

private fun populateTestData(database: TransitDatabase) {
    with(database) {
        stopQueries.insert(TestStops.mackenzieKing)
        stopQueries.insert(TestStops.mackenzieKing1A)
        stopQueries.insert(TestStops.mackenzieKing2A)
        stopQueries.insert(TestStops.walkleyJasper)
    }

    with(database) {
        routeQueries.insert(TestRoutes.route2)
        routeQueries.insert(TestRoutes.route44)
    }

    with(database) {
        recentSearchQueries.insert(TestRecent.mackenzieKing)
        recentSearchQueries.insert(TestRecent.route95)
        recentSearchQueries.insert(TestRecent.laurier110)
    }
}

private fun StopQueries.insert(stop: Stop.Impl) {
    insert(stop.id, stop.code, stop.name, stop.latitude, stop.longitude, stop.location_type, stop.parent_station)
    insertfts(
        stop.id.value,
        stop.code.value,
        stop.name,
        stop.latitude,
        stop.longitude,
        stop.location_type,
        stop.parent_station
    )
}

private fun RouteQueries.insert(route: Route.Impl) {
    insert(route.id, route.short_name, route.long_name, route.type, route.service_level, route.color)
}

private fun RecentSearchQueries.insert(recent: Recent_search.Impl) {
    insert(recent.id, recent.type, recent.date, recent.primary_text, recent.secondary_text, recent.number, recent.code)
}