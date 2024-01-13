package com.example.fooddiary.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fooddiary.database.Item
import com.example.fooddiary.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val itemRepository: ItemRepository) : ViewModel() {

    val itemsList : LiveData<List<Item>> = itemRepository.dataList
    val foundItem : LiveData<Item> = itemRepository.foundItem

    fun getAllItems(){
        itemRepository.getAllItems()
    }
    fun addItem(item: Item){
        itemRepository.addItem(item)
        itemRepository.getAllItems()
    }

    fun updateItem(item: Item){
        itemRepository.updateItem(item)
        itemRepository.getAllItems()
    }

    fun deleteItem(item: Item){
        itemRepository.deleteItem(item)
        itemRepository.getAllItems()
    }

    fun findItemById(id: Int){
        itemRepository.findItemById(id)
    }

}