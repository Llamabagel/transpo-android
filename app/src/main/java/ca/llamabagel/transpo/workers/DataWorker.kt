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

class DataWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dataRepository: DataRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val remoteMetadata = dataRepository.getRemoteAppMetadata()
        val localMetadata = dataRepository.getLocalAppMetadata()

        if (remoteMetadata.dataSchemaVersion != localMetadata.dataSchemaVersion
            || remoteMetadata.dataVersion <= localMetadata.dataVersion
        ) {
            return Result.success()
        }

        // Download and load new data
        dataRepository.updateLocalData()

        return Result.success()
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}


