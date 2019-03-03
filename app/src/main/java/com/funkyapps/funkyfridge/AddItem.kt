package com.funkyapps.funkyfridge

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class AddItem : AppCompatActivity() {

    var prodUPCCode = "12345"
    var calories : Int = -1
    var total_fat : Int = -1
    var total_carbs : Int = -1
    var sugars : Int = -1
    var protein : Int = -1
    var prodName : String = "No name"
    var date : String = ""
    var currentDate : String = ""
    lateinit var editProdName : EditText
    lateinit var editExpirDate : TextView
    lateinit var changeButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        editProdName = findViewById(R.id.edit_item_name)
        editExpirDate = findViewById(R.id.expirationDateTextView)
        changeButton = findViewById(R.id.changeDateButton)

        changeButton.setOnClickListener {
            val c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view2, thisYear, thisMonth, thisDay ->
                // Display Selected date in textbox
                year = thisMonth + 1
                month = thisDay
                day = thisYear

                val newDate:Calendar =Calendar.getInstance()
                newDate.set(thisYear, thisMonth, thisDay)
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val formatter2 = SimpleDateFormat("MM/dd/yyyy")
                date = formatter.format(newDate.time)
                val date2 = formatter2.format(newDate.time)
                editExpirDate.text = "Expires: " + date2
            }, year, month, day)
            dpd.show()
        }

        val fab: View = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val intent = Intent()
            if(date.equals("")){
                val c = Calendar.getInstance()
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                currentDate = formatter.format(c.time)
                intent.putExtra("date",currentDate)
            }
            else{
                intent.putExtra("date",date)
            }
            if(!editProdName.text.toString().equals("")){
                prodName = editProdName.text.toString()
            }

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

                    val queue = Volley.newRequestQueue(this)
                    val url = "http://api.nutritionix.com/v1_1/item?upc=" +
                        prodUPCCode + "&appId=359b6d3a&appKey=0f9a03a179334c56721fcf70cc8600b9"

                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.GET, url, null,
                        Response.Listener { response ->

                            // manage object
                            prodName = response.getString("item_name")
                            editProdName.setText(prodName)

                            calories = response.getInt("nf_calories")
                            total_fat = response.getInt("nf_total_fat")
                            total_carbs = response.getInt("nf_total_carbohydrate")
                            sugars = response.getInt("nf_suagrs")
                            protein = response.getInt("nf_protein")

                            // TODO: Add 5 macros, prodName and Expiration date to DB
                        },
                        Response.ErrorListener { error ->
                            // TODO: Handle error
                        }
                    )
                    queue.add(jsonObjectRequest)

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
