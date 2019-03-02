package com.funkyapps.funkyfridge

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.w3c.dom.Text
import java.sql.Date


@Entity(tableName = "food_item_table")
data class FoodItem(
    @ColumnInfo(name = "product") val product : String = String(),
    @ColumnInfo(name = "itemName") val itemName : String = String(),
    @ColumnInfo(name = "UPCid") val UPCid : Int,
    @ColumnInfo(name = "expDate") val expDate : String,
    @ColumnInfo(name = "carbs") val carbs : Int,
    @ColumnInfo(name = "proteins") val proteins : Int,
    @ColumnInfo(name = "sugars") val sugars : Int,
    @ColumnInfo(name = "fats") val fats : Int,
    @ColumnInfo(name = "calories") val calories : Int
    ){
    //Auto Generate the integer key id
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id : Int = 0


}