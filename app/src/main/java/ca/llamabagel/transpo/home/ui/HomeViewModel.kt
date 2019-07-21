/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import androidx.work.WorkInfo
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import ca.llamabagel.transpo.home.domain.GetLiveUpdatesDataUseCase
import ca.llamabagel.transpo.home.domain.UpdateLiveUpdatesUseCase
import ca.llamabagel.transpo.models.updates.LiveUpdate
import ca.llamabagel.transpo.transit.workers.DataWorker
import ca.llamabagel.transpo.transit.workers.RemoteMetadataWorker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getLiveUpdatesData: GetLiveUpdatesDataUseCase,
    private val updateLiveUpdates: UpdateLiveUpdatesUseCase
) : ViewModel() {
    private val workManager: WorkManager by lazy { WorkManager.getInstance() }

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _updateData = MutableLiveData<List<LiveUpdate>>()
    val updateData: LiveData<List<LiveUpdate>> = _updateData

    val workInfo: LiveData<List<WorkInfo>>

    init {
        workInfo = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

        viewModelScope.launch {
            getLiveUpdatesData().collect {
                _updateData.value = it
            }
        }
    }

    fun checkAndApplyDataUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresStorageNotLow(true)
            .build()

        val checkRequest = OneTimeWorkRequestBuilder<RemoteMetadataWorker>()
            .setConstraints(constraints)
            .build()
        val updateRequest = OneTimeWorkRequestBuilder<DataWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()

        workManager.beginWith(checkRequest)
            .then(updateRequest)
            .enqueue()
    }

    fun refreshLiveUpdates() = viewModelScope.launch {
        _isRefreshing.value = true
        updateLiveUpdates()
        _isRefreshing.value = false
    }

    companion object {
        const val TAG_OUTPUT = "data_worker_output"
    }
}
