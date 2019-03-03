package com.funkyapps.funkyfridge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


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

            val queue = Volley.newRequestQueue(this)
            val url = "http://my-json-feed"

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    // manage object
                    val prodName = response.getString("item_name")
                    editProdName.setText(prodName)

                    val calories = response.getInt("nf_calories")
                    val total_fat = response.getInt("nf_total_fat")
                    val total_carbs = response.getInt("nf_total_carbohydrate")
                    val sugars = response.getInt("nf_suagrs")
                    val protein = response.getInt("nf_protein")

                    // Add 5 macros, prodName and Expiration date to DB
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                }
            )
            queue.add(jsonObjectRequest)

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
