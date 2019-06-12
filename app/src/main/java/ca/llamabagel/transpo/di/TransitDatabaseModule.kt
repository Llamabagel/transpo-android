/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import ca.llamabagel.transpo.data.db.Stop
import ca.llamabagel.transpo.data.db.StopCode
import ca.llamabagel.transpo.data.db.StopId
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TransitDatabaseModule {

    @Provides
    @Singleton
    fun provideTransitDatabase(sqlDriver: SqlDriver, stopAdapter: Stop.Adapter): TransitDatabase =
        TransitDatabase(sqlDriver, stopAdapter)

    @Provides
    @Singleton
    fun provideSqlDriver(context: Context): SqlDriver =
        AndroidSqliteDriver(TransitDatabase.Schema, context, "transit.db")

    @Provides
    @Singleton
    fun provideStopAdapter(): Stop.Adapter = STOP_ADAPTER

    companion object {
        private val stopIdAdapter = object : ColumnAdapter<StopId, String> {
            override fun decode(databaseValue: String): StopId = StopId(databaseValue)

            override fun encode(value: StopId): String = value.value
        }

        val STOP_ADAPTER = Stop.Adapter(
            idAdapter = stopIdAdapter,
            codeAdapter = object : ColumnAdapter<StopCode, String> {
                override fun decode(databaseValue: String): StopCode = StopCode(databaseValue)

                override fun encode(value: StopCode): String = value.value
            },
            parent_stationAdapter = stopIdAdapter
        )
    }
}