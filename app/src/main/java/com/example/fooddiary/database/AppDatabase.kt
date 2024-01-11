package com.example.fooddiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [DBItem::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun customDao(): CustomDao?

    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase? {
            //context.deleteDatabase("item_database")
            if (DB_INSTANCE == null){
                DB_INSTANCE = databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "item_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE
        }
    }

}