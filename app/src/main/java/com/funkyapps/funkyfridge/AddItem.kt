package com.funkyapps.funkyfridge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import org.json.JSONException
import org.json.JSONObject

class AddItem : AppCompatActivity() {

    var prodUPCCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val editProdName = findViewById<EditText>(R.id.edit_item_name)
            val editExpirDate = findViewById<EditText>(R.id.edit_expiration_date)

            // TODO: add alert saying to scan barcode
            if (prodUPCCode == "") {

            }

            lateinit var json: JSONObject

            try {
                var json = JSONObject("https://api.nutritionix.com/v1_1/item?upc=" +
                        prodUPCCode + "&appId=359b6d3a&appKey=0f9a03a179334c56721fcf70cc8600b9")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val prodName = json.getString("item_name")
            editProdName.setText(prodName)

            val calories = json.getInt("nf_calories")
            val total_fat = json.getInt("nf_total_fat")
            val total_carbs = json.getInt("nf_total_carbohydrate")
            val sugars = json.getInt("nf_suagrs")
            val protein = json.getInt("nf_protein")

            // TODO: add these vals, editProdName.text and expiration date to DB

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
