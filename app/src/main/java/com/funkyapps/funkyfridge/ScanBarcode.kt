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
        createCameraSource()
    }

    fun createCameraSource() {
        val barcodeDetector = BarcodeDetector.Builder(this).build()
        val cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1600, 1024)
            .build()

        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanBarcode,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED) {

                    return
                }
                try {
                    cameraSource.start(cameraPreview.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

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
}
