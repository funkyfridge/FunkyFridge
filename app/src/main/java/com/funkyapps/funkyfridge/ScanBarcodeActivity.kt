package com.funkyapps.funkyfridge

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import org.w3c.dom.Text

import com.google.android.gms.vision.barcode.BarcodeDetector

import java.io.IOException

class ScanBarcodeActivity : Activity() {
    protected lateinit var cameraPreview: SurfaceView;

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
                if (ActivityCompat.checkSelfPermission(
                        this@ScanBarcodeActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                try {
                    cameraSource.start(cameraPreview.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {

            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                // creates a SparseArray of barcodes from detected items
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val intent = Intent()
                    //gets latest barcode from array
                    intent.putExtra("barcode", barcodes.valueAt(0))
                    setResult(CommonStatusCodes.SUCCESS, intent)
                    finish()
                }
            }
        })
    }
}
