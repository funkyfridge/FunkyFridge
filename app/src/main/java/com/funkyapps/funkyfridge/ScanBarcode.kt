package com.funkyapps.funkyfridge

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import android.support.v4.content.ContextCompat
import android.widget.Toast


class ScanBarcode : AppCompatActivity() {

    protected lateinit var cameraPreview: SurfaceView
    protected lateinit var barcodeDetector: BarcodeDetector
    protected lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
        cameraPreview = findViewById<SurfaceView>(R.id.camera_preview)
        createCameraSource()
    }

    fun createCameraSource() {
        barcodeDetector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1600, 1024)
            .build()

        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {}
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ContextCompat.checkSelfPermission(
                        this@ScanBarcode,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED)
                    cameraSource.start(holder)
                else
                    ActivityCompat.requestPermissions(
                        this@ScanBarcode,
                        arrayOf(Manifest.permission.CAMERA),
                        123)
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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                cameraSource.start(cameraPreview.holder)
            else
                Toast.makeText(this, "Scanner won't work without permission.", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeDetector.release()
        cameraSource.stop()
        cameraSource.release()
    }
}
