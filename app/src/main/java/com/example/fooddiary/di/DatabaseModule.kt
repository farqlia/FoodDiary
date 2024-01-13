package com.example.fooddiary.di

import android.content.Context
import androidx.room.Room
import com.example.fooddiary.database.ItemDao
import com.example.fooddiary.database.ItemRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
private object DatabaseModule {

    @Provides
    fun provideItemDao(appDatabase: ItemRoomDatabase): ItemDao {
        return appDatabase.itemDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ItemRoomDatabase {
        return Room.databaseBuilder(
            context,
            ItemRoomDatabase::class.java,
            "appDB"
        ).build()
    }

}