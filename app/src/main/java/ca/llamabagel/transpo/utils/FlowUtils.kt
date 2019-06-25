package ca.llamabagel.transpo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

suspend fun <T> Flow<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    collect { liveData.value = it }
    return liveData
}