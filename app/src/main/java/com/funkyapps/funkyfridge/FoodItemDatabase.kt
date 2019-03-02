package com.funkyapps.funkyfridge

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(FoodItem::class),version = 1)
public abstract class FoodItemDatabase : RoomDatabase() {
    abstract fun fItemDAO() : FoodItemDAO

    //Make the DB a singleton
    companion object {
        @Volatile
        private var INSTANCE : FoodItemDatabase? = null

        fun getDatabase(context : Context) : FoodItemDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodItemDatabase::class.java,
                    "Word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}