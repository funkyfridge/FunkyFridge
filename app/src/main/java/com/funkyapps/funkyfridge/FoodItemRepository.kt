package com.funkyapps.funkyfridge

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import android.support.annotation.WorkerThread
import kotlinx.coroutines.*
import java.util.ArrayList
import java.util.concurrent.ExecutionException
import kotlin.coroutines.*

class FoodItemRepository(application : Application) : AndroidViewModel(application){
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private var fItemDAO : FoodItemDAO

    init{
        fItemDAO = FoodItemDatabase.getDatabase(application,scope).fItemDAO()
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

    fun getAllFoodItems(): MutableList<FoodItem> {
        val foodItems = ArrayList<FoodItem>()
        try {
            return getAllModsAsyncTask(fItemDAO).execute().get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        return foodItems
    }

    private class getAllModsAsyncTask internal constructor(
        private val mAsyncFoodDao: FoodItemDAO
        ) : AsyncTask<Void, Void, MutableList<FoodItem>>() {
        private val mods: MutableList<FoodItem>? = null

        override fun doInBackground(vararg params: Void): MutableList<FoodItem> {
            return mAsyncFoodDao.getAllFoodItems()
        }

    }
}