/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.data.db

import ca.llamabagel.transpo.models.transit.Route
import ca.llamabagel.transpo.models.transit.Stop

data class RouteWithStops(val route: Route, val stops: List<Stop>)