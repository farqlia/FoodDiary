package com.example.fooddiary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY id ASC")
    fun getAll(): MutableList<Item>

    @Query("DELETE FROM items")
    fun deleteAll()

    @Query("SELECT * FROM items WHERE id=:itemId")
    fun findItemById(itemId: Int) : Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Item): Long

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item): Int

}