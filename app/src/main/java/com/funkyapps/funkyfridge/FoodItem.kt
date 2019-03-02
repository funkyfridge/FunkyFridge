package com.funkyapps.funkyfridge

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "food_item_table")
data class FoodItem(
    //Auto Generate the integer key id
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int,
    @ColumnInfo(name = "product") val product : String = String(),
    @ColumnInfo(name = "itemName") val itemName : String = String(),
    @ColumnInfo(name = "UPCid") val UPCid : Int,
    @ColumnInfo (name = "expDate") val expDate : Date
    ){



}