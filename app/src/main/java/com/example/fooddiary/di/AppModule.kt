package com.example.fooddiary.di

import com.example.fooddiary.database.ItemDao
import com.example.fooddiary.repositories.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideItemRepository(itemDao: ItemDao): ItemRepository {
        return ItemRepository(itemDao)
    }

}