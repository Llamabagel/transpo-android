/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.home

import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ca.llamabagel.transpo.data.DataRepository
import ca.llamabagel.transpo.workers.DataWorker

class MainViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val workManager = WorkManager.getInstance()

    fun checkAndApplyDataUpdates() {
       workManager.enqueue(OneTimeWorkRequest.from(DataWorker::class.java))
    }
}