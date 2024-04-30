package com.twofasapp.feature.qrscan

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.twofasapp.feature.CaptureActivityPortrait

@Composable
fun QrScan(
    onScanned: (String) -> Unit = {},
) {
    val scanLauncher =
        rememberLauncherForActivityResult(
            contract = ScanContract(),
            onResult = {
                if(it.contents != null) {
                    onScanned(it.contents)
                }
            },
        )
    LaunchedEffect(Unit) {
        val scanOptions = ScanOptions()
        scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        scanOptions.setOrientationLocked(true)
        scanOptions.setBeepEnabled(false)
        scanOptions.captureActivity =
            CaptureActivityPortrait::class.java
        scanLauncher.launch(scanOptions)
    }
}