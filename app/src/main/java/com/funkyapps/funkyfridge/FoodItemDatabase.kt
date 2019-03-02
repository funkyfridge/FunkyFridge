package com.funkyapps.funkyfridge

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

@Database(entities = arrayOf(FoodItem::class),version = 1)
public abstract class FoodItemDatabase : RoomDatabase() {
    abstract fun fItemDAO() : FoodItemDAO

    //Make the DB a singleton
    companion object {
        @Volatile
        private var INSTANCE : FoodItemDatabase? = null

        fun getDatabase(context : Context, scope: CoroutineScope) : FoodItemDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodItemDatabase::class.java,
                    "food_item_database"
                ).addCallback(FoodItemDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }

        private class FoodItemDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.fItemDAO())
                    }
                }
            }
            fun populateDatabase(fItemDAO: FoodItemDAO) {
                fItemDAO.deleteAllFoodItems()

                var foodItem = FoodItem("Milk", "Milk 1", 1, "2019-03-02 13:52:33.213")
                fItemDAO.insert(foodItem)
                foodItem = FoodItem("Milk", "Milk 2", 1, "2019-03-02 13:53:45.213")
                fItemDAO.insert(foodItem)
            }
        }
    }
}