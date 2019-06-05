/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    var skippedCount = 0
    mediator.addSource(this) {
        if (skippedCount >= count) {
            mediator.value = it
        }
        skippedCount++
    }

    return mediator
}

fun <T, R> LiveData<T>.scan(initial: R, accumulator: (accumulatedValue: R, currentValue: T) -> R): MutableLiveData<R> {
    var accumulatedValue = initial

    return MediatorLiveData<R>().apply {
        addSource(this@scan) { emittedValue ->
            accumulatedValue = accumulator(accumulatedValue, emittedValue!!)
            value = accumulatedValue
        }
    }
}