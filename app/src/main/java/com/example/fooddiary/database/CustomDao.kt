package com.example.fooddiary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CustomDao {

    @Query("SELECT * FROM item_table ORDER BY id ASC")
    fun getAll(): MutableList<DBItem>

    @Query("DELETE FROM item_table")
    fun deleteAll()

    @Query("SELECT * FROM item_table WHERE id=:itemId")
    fun findItemById(itemId: Int) : DBItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: DBItem): Long

    @Update
    fun update(item: DBItem)

    @Delete
    fun delete(item: DBItem): Int

}