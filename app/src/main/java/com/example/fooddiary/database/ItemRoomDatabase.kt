package com.example.fooddiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao
    companion object {

        private var DB_INSTANCE: ItemRoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ItemRoomDatabase? {
            //context.deleteDatabase("item_database")
            if (DB_INSTANCE == null){
                DB_INSTANCE = databaseBuilder(context.applicationContext,
                    ItemRoomDatabase::class.java, "item_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return DB_INSTANCE
        }
    }

}