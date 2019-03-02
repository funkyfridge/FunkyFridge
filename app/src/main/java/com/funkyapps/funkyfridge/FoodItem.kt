package com.funkyapps.funkyfridge

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.w3c.dom.Text
import java.sql.Date


@Entity(tableName = "food_item_table")
data class FoodItem(
    @ColumnInfo(name = "product") var product : String = String(),
    @ColumnInfo(name = "itemName") var itemName : String = String(),
    @ColumnInfo(name = "UPCid") var UPCid : Int,
    @ColumnInfo(name = "expDate") var expDate : String,
    @ColumnInfo(name = "carbs") var carbs : Int,
    @ColumnInfo(name = "proteins") var proteins : Int,
    @ColumnInfo(name = "sugars") var sugars : Int,
    @ColumnInfo(name = "fats") var fats : Int,
    @ColumnInfo(name = "calories") var calories : Int
    ){
    //Auto Generate the integer key id
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id : Int = 0


}