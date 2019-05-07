/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import androidx.work.ListenableWorker
import ca.llamabagel.transpo.workers.ChildWorkerFactory
import ca.llamabagel.transpo.workers.DataWorker
import ca.llamabagel.transpo.workers.RemoteMetadataWorker
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(DataWorker::class)
    fun bindDataWorker(factory: DataWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(RemoteMetadataWorker::class)
    fun bindRemoteMetadataWorker(factory: RemoteMetadataWorker.Factory): ChildWorkerFactory
}

@AssistedModule
@Module(includes = [AssistedInject_AssistedWorkerInjectModule::class])
interface AssistedWorkerInjectModule