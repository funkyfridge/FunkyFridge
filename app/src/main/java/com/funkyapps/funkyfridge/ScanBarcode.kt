package com.funkyapps.funkyfridge

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.support.v4.content.ContextCompat
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.support.v7.app.AlertDialog
import android.support.v4.content.ContextCompat.startActivity
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.app.Activity
import android.net.Uri
import android.provider.Settings


class ScanBarcode : AppCompatActivity() {

    val MY_PERMISSIONS_REQUEST_CAMERA = 100
    val ALLOW_KEY = "ALLOWED"
    val CAMERA_PREF = "camera_pref"

    protected lateinit var cameraPreview: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
        cameraPreview = findViewById<SurfaceView>(R.id.camera_preview)
    }

    fun createCameraSource() {
        val barcodeDetector = BarcodeDetector.Builder(this).build()
        val cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1600, 1024)
            .build()

        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ContextCompat.checkSelfPermission(
                        this@ScanBarcode,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(this@ScanBarcode, ALLOW_KEY)) {
                        showSettingsAlert()
                    }
                    else if (ContextCompat.checkSelfPermission(
                            this@ScanBarcode,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this@ScanBarcode,
                                Manifest.permission.CAMERA)) {
                            showAlert()
                        }
                        else {
                            ActivityCompat.requestPermissions(
                                this@ScanBarcode,
                                Array(1, {"Manifest.permission.CAMERA"}),
                                MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                }
                else {
                    openCamera()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object: Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val intent = Intent()
                    intent.putExtra("barcode", barcodes.valueAt(0))
                    setResult(CommonStatusCodes.SUCCESS, intent)
                    finish()
                }
            }
        })
    }

    private fun showAlert() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("App needs to access the Camera.")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW")
        { dialog, which ->
            dialog.dismiss()
            finish()
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
            object:DialogInterface.OnClickListener {
                override fun onClick(dialog:DialogInterface, which:Int) {
                    dialog.dismiss()
                    ActivityCompat.requestPermissions(this@ScanBarcode,
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA)
                }
            })

        alertDialog.show()
    }

    fun getFromPref(context: Context, key: String): Boolean {
        val myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE)
        return myPrefs.getBoolean(key, false)
    }

    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("App needs to access the Camera.")
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                //finish();
            })
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                startInstalledAppDetailsActivity(this@ScanBarcode)
            })
        alertDialog.show()
    }

    fun startInstalledAppDetailsActivity(context: Activity?) {
        if (context == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + context.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(i)
    }

    private fun openCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }
}
