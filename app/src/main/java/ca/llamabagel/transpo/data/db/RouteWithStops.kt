/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import ca.llamabagel.transpo.models.app.Route
import ca.llamabagel.transpo.models.app.Stop

data class RouteWithStops(val route: Route, val stops: List<Stop>)