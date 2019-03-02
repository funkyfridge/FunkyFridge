package com.funkyapps.funkyfridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var foodItemRepo : FoodItemRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        foodItemRepo = FoodItemRepository(application)

        val textView : TextView = findViewById(R.id.dbTextview)
        val button : Button = findViewById(R.id.dbButton)

        button.setOnClickListener {
            var string : String = ""
            val foodItems : List<FoodItem> = foodItemRepo.getAllFoodItems()
            for(item in foodItems){
                string += item.itemName + "\n"
            }
            textView.setText(string);
        }
    }
}
