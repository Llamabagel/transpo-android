/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

enum class KeyboardState {
    OPEN, CLOSED
}

class SearchViewModel @Inject constructor() : ViewModel() {

    private val _keyboardState = MutableLiveData<KeyboardState>()
    val keyboardState = _keyboardState as LiveData<KeyboardState>

    init {
        _keyboardState.value = KeyboardState.OPEN
    }

    fun notifyClosed() {
        _keyboardState.value = KeyboardState.CLOSED
    }
}