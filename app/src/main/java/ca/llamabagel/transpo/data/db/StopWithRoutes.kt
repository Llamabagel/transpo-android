/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import ca.llamabagel.transpo.models.app.Route
import ca.llamabagel.transpo.models.app.Stop

data class StopWithRoutes(val stop: Stop, val routes: List<Route>)