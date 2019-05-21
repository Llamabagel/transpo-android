/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import ca.llamabagel.transpo.data.DataRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class RemoteMetadataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dataRepository: DataRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val remoteMetadata = dataRepository.getRemoteAppMetadata()
        val localMetadata = dataRepository.getLocalAppMetadata()

        val data = Data.Builder()

        if (remoteMetadata.dataSchemaVersion != localMetadata.dataSchemaVersion
            || remoteMetadata.dataVersion <= localMetadata.dataVersion
        ) {
            data.putBoolean(KEY_UPDATE, false)
        } else {
            data.putBoolean(KEY_UPDATE, true)
        }
        data.putString(KEY_REMOTE_VERSION, remoteMetadata.dataVersion)

        return Result.success(data.build())
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}