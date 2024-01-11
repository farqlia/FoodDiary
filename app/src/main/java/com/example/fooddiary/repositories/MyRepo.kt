package com.example.fooddiary.repositories

import androidx.lifecycle.MutableLiveData
import com.example.fooddiary.database.CustomDao
import com.example.fooddiary.database.DBItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRepo(private val customDao: CustomDao) {

    val dataList = MutableLiveData<List<DBItem>>()
    val foundItem = MutableLiveData<DBItem>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    fun addItem(item: DBItem) {
        coroutineScope.launch(Dispatchers.IO){
            customDao.insert(item)
        }
    }

    fun deleteItem(item: DBItem) {
        coroutineScope.launch(Dispatchers.IO){
            customDao.delete(item)
        }
    }

    fun updateItem(item: DBItem) {
        coroutineScope.launch(Dispatchers.IO){
            customDao.update(item)
        }
    }

    fun getAllItems(){
        coroutineScope.launch(Dispatchers.IO){
            dataList.postValue(customDao.getAll())
        }
    }

    fun findItemById(id: Int){
        coroutineScope.launch(Dispatchers.IO){
            foundItem.postValue(customDao.findItemById(id))
        }
    }

}