/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.di

import android.content.Context
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TransitDatabaseModule {

    @Provides
    @Singleton
    fun provideTransitDatabase(sqlDriver: SqlDriver): TransitDatabase = TransitDatabase(sqlDriver)

    @Provides
    @Singleton
    fun provideSqlDriver(context: Context): SqlDriver =
        AndroidSqliteDriver(TransitDatabase.Schema, context, "transit.db")
}