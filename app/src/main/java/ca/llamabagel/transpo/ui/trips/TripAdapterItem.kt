/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.trips

sealed class TripAdapterItem

data class TripItem(val tripUiModel: TripUiModel) : TripAdapterItem()