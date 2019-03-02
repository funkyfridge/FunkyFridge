package com.funkyapps.funkyfridge

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface FoodItemDAO {

    @Insert
    fun insert(item : FoodItem)

    @Update
    fun update(item : FoodItem)

    @Query("SELECT * from food_item_table f WHERE f.id = :id")
    fun getFoodItem(id : Int) : FoodItem

    @Query("SELECT * from food_item_table")
    fun getAllFoodItems() : List<FoodItem>

    @Query("DELETE from food_item_table")
    fun deleteAllFoodItems()

    @Query("DELETE from food_item_table WHERE id = :id")
    fun deleteFoodItem(id : Int)

}