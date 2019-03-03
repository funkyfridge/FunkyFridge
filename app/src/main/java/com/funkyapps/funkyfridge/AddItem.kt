package com.funkyapps.funkyfridge

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AddItem : AppCompatActivity() {

    var prodUPCCode = "12345"
    var calories : Int = -1
    var total_fat : Int = -1
    var total_carbs : Int = -1
    var sugars : Int = -1
    var protein : Int = -1
    var prodName : String = "Milky"
    lateinit var editProdName : EditText
    lateinit var editExpirDate : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        editProdName = findViewById<EditText>(R.id.edit_item_name)
        editExpirDate = findViewById<EditText>(R.id.edit_expiration_date)
        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val intent : Intent = Intent()
            intent.putExtra("calories",calories)
            intent.putExtra("fat",total_fat)
            intent.putExtra("carbs",total_carbs)
            intent.putExtra("sugars",sugars)
            intent.putExtra("protein",protein)
            intent.putExtra("name",prodName)
            intent.putExtra("upc",prodUPCCode)
            setResult(Activity.RESULT_OK,intent)
            finish()
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
                    lateinit var json: JSONObject

                    try {
                        //Bug opening buffered reader or something
                        val url = URL("https://api.nutritionix.com/v1_1/item?upc=" +
                                prodUPCCode + "&appId=359b6d3a&appKey=0f9a03a179334c56721fcf70cc8600b9")
                        val conn = url.openConnection() as HttpURLConnection
                        conn.requestMethod = "GET"
                        val rd = BufferedReader(InputStreamReader(conn.inputStream))
                        json = JSONObject(rd.readText())

                    } catch (e: JSONException) {
                        Toast.makeText(this,"Barcode could not be identified, sorry.",Toast.LENGTH_SHORT)
                        e.printStackTrace()
                    }

                    prodName = json.getString("item_name")
                    editProdName.setText(prodName)

                    calories = json.getInt("nf_calories")
                    total_fat = json.getInt("nf_total_fat")
                    total_carbs = json.getInt("nf_total_carbohydrate")
                    sugars = json.getInt("nf_sugars")
                    protein = json.getInt("nf_protein")
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
