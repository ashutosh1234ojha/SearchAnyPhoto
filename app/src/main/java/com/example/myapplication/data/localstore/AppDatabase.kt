package com.example.myapplication.data.localstore

import android.arch.persistence.room.*
import android.content.Context
import com.example.myapplication.utility.Constants

@Database(entities = [(PhotoData::class)], version = 1)
@TypeConverters(DataConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PhotoDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, Constants.DB_NAME)
                        .allowMainThreadQueries().build()
    }
}
