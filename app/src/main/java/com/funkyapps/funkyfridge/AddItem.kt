package com.funkyapps.funkyfridge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class AddItem : AppCompatActivity() {

    val prodUPCCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val editProdName = findViewById<EditText>(R.id.edit_item_name)
            val editExpirDate = findViewById<EditText>(R.id.edit_expiration_date)

            // TODO: throw error msg
            if (prodUPCCode == 0) {

            }

            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }
    }

    fun scanBarcode(view: View) {


        prodUPCCode =
    }
}
