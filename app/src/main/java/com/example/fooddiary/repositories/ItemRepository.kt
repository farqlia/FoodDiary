package com.example.fooddiary.repositories

import androidx.lifecycle.MutableLiveData
import com.example.fooddiary.database.ItemDao
import com.example.fooddiary.database.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemRepository(private val itemDao: ItemDao) {

    val dataList = MutableLiveData<List<Item>>()
    val foundItem = MutableLiveData<Item>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    fun addItem(item: Item) {
        coroutineScope.launch(Dispatchers.IO){
            itemDao.insert(item)
        }
    }

    fun deleteItem(item: Item) {
        coroutineScope.launch(Dispatchers.IO){
            itemDao.delete(item)
        }
    }

    fun updateItem(item: Item) {
        coroutineScope.launch(Dispatchers.IO){
            itemDao.update(item)
        }
    }

    fun getAllItems(){
        coroutineScope.launch(Dispatchers.IO){
            dataList.postValue(itemDao.getAll())
        }
    }

    fun findItemById(id: Int){
        coroutineScope.launch(Dispatchers.IO){
            foundItem.postValue(itemDao.findItemById(id))
        }
    }

}