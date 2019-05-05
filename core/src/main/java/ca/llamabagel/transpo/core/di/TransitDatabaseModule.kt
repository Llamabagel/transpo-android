/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.core.di

import android.content.Context
import ca.llamabagel.transpo.core.di.scope.FeatureScope
import ca.llamabagel.transpo.data.db.TransitDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides

@Module
class TransitDatabaseModule(val context: Context) {

    @Provides
    @FeatureScope
    fun provideTransitDatabase(sqlDriver: SqlDriver): TransitDatabase = TransitDatabase(sqlDriver)

    @Provides
    @FeatureScope
    fun provideSqlDriver(): SqlDriver = AndroidSqliteDriver(TransitDatabase.Schema, context, "transit.db")

}