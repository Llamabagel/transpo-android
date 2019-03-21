/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.llamabagel.transpo.models.app.Route
import ca.llamabagel.transpo.models.app.RouteShape
import ca.llamabagel.transpo.models.app.Stop
import ca.llamabagel.transpo.models.app.StopRoute

@Database(entities = [Stop::class, Route::class, RouteShape::class, StopRoute::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stopDao(): StopDao
    abstract fun routeDao(): RouteDao
    abstract fun routeShapeDao(): RouteShapeDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "data.db")
                .build()
        }
    }
}