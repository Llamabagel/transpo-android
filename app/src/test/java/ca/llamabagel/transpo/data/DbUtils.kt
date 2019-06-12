/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data

import ca.llamabagel.transpo.data.db.*
import ca.llamabagel.transpo.di.TransitDatabaseModule.Companion.STOP_ADAPTER
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

fun getDatabase(): TransitDatabase {
    val driver = JdbcSqliteDriver()
    TransitDatabase.Schema.create(driver)

    val database = TransitDatabase(driver, STOP_ADAPTER)
    populateTestData(database)

    return database
}

object TestStops {
    val mackenzieKing1A = Stop.Impl(StopId("CD900"), StopCode("3000"), "Mackenzie King 1A", 45.42414, -75.6893711, 0, StopId("MAC"))
    val mackenzieKing2A = Stop.Impl(StopId("CD910"), StopCode("3000"), "Mackenzie King 2A", 45.424126, -75.689985, 0, StopId("MAC"))
    val mackenzieKing = Stop.Impl(StopId("MAC"), StopCode("3000"), "Mackenzie King Station", 45.424237, -75.689459, 1, null)

    val walkleyJasper = Stop.Impl(StopId("AH060"), StopCode("7196"), "Walkley / Jasper", 45.373063, -75.656894, 0, null)
}

object TestRoutes {
    val route2 = Route.Impl("2-288", "2", "", 2, "", "")
    val route44 = Route.Impl("44-288", "44", "", 3, "", "")
}

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
}

private fun StopQueries.insert(stop: Stop.Impl) {
    insert(stop.id, stop.code, stop.name, stop.latitude, stop.longitude, stop.location_type, stop.parent_station)
}

private fun RouteQueries.insert(route: Route.Impl) {
    insert(route.id, route.short_name, route.long_name, route.type, route.service_level, route.color)
}