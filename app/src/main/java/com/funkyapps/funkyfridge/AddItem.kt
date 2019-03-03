package com.funkyapps.funkyfridge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode

class AddItem : AppCompatActivity() {

    var prodUPCCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val editProdName = findViewById<EditText>(R.id.edit_item_name)
            val editExpirDate = findViewById<EditText>(R.id.edit_expiration_date)

            // TODO: throw error msg
            if (prodUPCCode == "") {

            }

            // TODO: add prodUPCCode to database
        }
    }
    fun scanBarcode(view: View) {
        val intent = Intent(this, ScanBarcode::class.java)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>("barcode")
                    prodUPCCode = barcode.displayValue
                }
                else {
                    prodUPCCode = ""
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
