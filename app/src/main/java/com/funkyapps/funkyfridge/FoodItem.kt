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
    @ColumnInfo(name = "UPCid") var UPCid : String = String(),
    @ColumnInfo(name = "expDate") var expDate : String,
    @ColumnInfo(name = "carbs") var carbs : Int = -1,
    @ColumnInfo(name = "proteins") var proteins : Int = -1,
    @ColumnInfo(name = "sugars") var sugars : Int = -1,
    @ColumnInfo(name = "fats") var fats : Int = -1,
    @ColumnInfo(name = "calories") var calories : Int = -1
    ){
    //Auto Generate the integer key id
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id : Int = 0


}