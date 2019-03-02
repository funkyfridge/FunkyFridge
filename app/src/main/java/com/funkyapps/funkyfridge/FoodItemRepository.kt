package com.funkyapps.funkyfridge

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.support.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlin.coroutines.*

class FoodItemRepository(application : Application, var fItemDAO : FoodItemDAO) : AndroidViewModel(application){
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init{
        fItemDAO = FoodItemDatabase.getDatabase(application).fItemDAO()
    }

    fun insert(foodItem : FoodItem) = scope.launch(Dispatchers.IO){
        fItemDAO.insert(foodItem)
    }

    fun update(foodItem : FoodItem) = scope.launch(Dispatchers.IO){
        fItemDAO.update(foodItem)
    }

    fun delete(foodItem : FoodItem) = scope.launch(Dispatchers.IO){
        fItemDAO.deleteFoodItem(foodItem.id)
    }

    fun deleteAll() = scope.launch(Dispatchers.IO){
        fItemDAO.deleteAllFoodItems()
    }

    fun getAllFoodItems() = scope.launch(Dispatchers.IO){
        fItemDAO.getAllFoodItems()
    }


}