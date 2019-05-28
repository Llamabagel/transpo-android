/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ca.llamabagel.transpo.data.DataRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

const val KEY_UPDATE = "key_update"
const val KEY_REMOTE_VERSION = "key_remote_version"

class DataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dataRepository: DataRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val update = inputData.getBoolean(KEY_UPDATE, false)

        if (update) {
            // Download and load new data
            dataRepository.updateLocalData()
        }

        return Result.success()
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}
