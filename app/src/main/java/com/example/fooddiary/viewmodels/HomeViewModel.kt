package com.example.fooddiary.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fooddiary.database.DBItem
import com.example.fooddiary.repositories.MyRepo
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val myRepo: MyRepo) : ViewModel() {

    val itemsList : LiveData<List<DBItem>> = myRepo.dataList
    val foundItem : LiveData<DBItem> = myRepo.foundItem

    fun getAllItems(){
        myRepo.getAllItems()
    }

    fun addItem(item: DBItem){
        myRepo.addItem(item)
        myRepo.getAllItems()
    }


    fun updateItem(item: DBItem){
        myRepo.updateItem(item)
        myRepo.getAllItems()
    }

    fun deleteItem(item: DBItem){
        myRepo.deleteItem(item)
        myRepo.getAllItems()
    }

    fun findItemById(id: Int){
        myRepo.findItemById(id)
    }

}