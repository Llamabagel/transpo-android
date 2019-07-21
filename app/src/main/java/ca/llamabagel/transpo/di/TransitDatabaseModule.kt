/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.annotation.SuppressLint
import android.content.Context
import ca.llamabagel.transpo.data.db.*
import ca.llamabagel.transpo.search.data.SearchFilters
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class TransitDatabaseModule {

    @Provides
    @Singleton
    fun provideTransitDatabase(
        sqlDriver: SqlDriver,
        recentAdapter: Recent_search.Adapter,
        stopAdapter: Stop.Adapter,
        liveUpdateAdapter: LiveUpdate.Adapter
    ): TransitDatabase = TransitDatabase(sqlDriver, liveUpdateAdapter, recentAdapter, stopAdapter)

    @Provides
    @Singleton
    fun provideSqlDriver(context: Context): SqlDriver =
        AndroidSqliteDriver(TransitDatabase.Schema, context, "transit.db")

    @Provides
    @Singleton
    fun provideRecentAdapter(): Recent_search.Adapter = RECENT_ADAPTER

    @Provides
    @Singleton
    fun provideStopAdapter(): Stop.Adapter = STOP_ADAPTER

    @Provides
    @Singleton
    fun provideLiveUpdateAdapter(): LiveUpdate.Adapter = LIVE_UPDATE_DATE_ADAPTER

    companion object {

        private val stopIdAdapter = object : ColumnAdapter<StopId, String> {
            override fun decode(databaseValue: String): StopId = StopId(databaseValue)

            override fun encode(value: StopId): String = value.value
        }

        val RECENT_ADAPTER = Recent_search.Adapter(
            typeAdapter = object : ColumnAdapter<SearchFilters, Long> {
                override fun decode(databaseValue: Long): SearchFilters = SearchFilters.values()[databaseValue.toInt()]

                override fun encode(value: SearchFilters): Long = value.ordinal.toLong()
            }
        )

        val STOP_ADAPTER = Stop.Adapter(
            idAdapter = stopIdAdapter,
            codeAdapter = object : ColumnAdapter<StopCode, String> {
                override fun decode(databaseValue: String): StopCode = StopCode(databaseValue)

                override fun encode(value: StopCode): String = value.value
            }
        )

        @SuppressLint("SimpleDateFormat")
        val LIVE_UPDATE_DATE_ADAPTER = LiveUpdate.Adapter(
            dateAdapter = object : ColumnAdapter<Date, String> {
                override fun decode(databaseValue: String): Date =
                    SimpleDateFormat("yyyy MM dd HH:mm:ss z").parse(databaseValue)!!

                override fun encode(value: Date): String = SimpleDateFormat("yyyy MM dd HH:mm:ss z").format(value)
            }
        )
    }
}