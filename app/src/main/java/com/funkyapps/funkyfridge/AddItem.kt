package com.funkyapps.funkyfridge

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

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
        }
    }

    fun scanBarcode(view: View) {
        val intent = Intent()
        startActivityForResult(intent, 0)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Int {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                val barcode = data.getParcelableExtra<Barcode>("barcode")
                return barcode.displayValue.toInt()
            }
        }
        // No barcode found
        return 0;
    }
}
