/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import androidx.work.WorkInfo
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import ca.llamabagel.transpo.transit.data.DataRepository
import ca.llamabagel.transpo.transit.workers.DataWorker
import ca.llamabagel.transpo.transit.workers.RemoteMetadataWorker

class MainViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val workManager: WorkManager by lazy { WorkManager.getInstance() }

    val workInfo: LiveData<List<WorkInfo>>

    init {
        workInfo = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
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

    companion object {
        const val TAG_OUTPUT = "data_worker_output"
    }
}