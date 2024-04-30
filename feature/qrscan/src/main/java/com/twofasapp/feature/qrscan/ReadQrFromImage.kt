package com.twofasapp.feature.qrscan

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.zxing.BinaryBitmap
import com.google.zxing.ChecksumException
import com.google.zxing.FormatException
import com.google.zxing.LuminanceSource
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class ReadQrFromImage(private val context: Context) {

    suspend operator fun invoke(uri: Uri): Result<String> = suspendCancellableCoroutine { continuation ->
        try {

            continuation.resume(decodeQRImage(uri.path))

        } catch (e: Exception) {
            e.printStackTrace()
            continuation.resume(Result.failure(e))
        }
    }

    private fun decodeQRImage(path: String?): Result<String> {
        val bMap = BitmapFactory.decodeFile(path)

        val intArray = IntArray(bMap.width * bMap.height)
        bMap.getPixels(
            intArray, 0, bMap.width, 0, 0, bMap.width,
            bMap.height
        )
        val source: LuminanceSource = RGBLuminanceSource(
            bMap.width,
            bMap.height, intArray
        )
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val reader: Reader = QRCodeReader()
        return try {
            val result = reader.decode(bitmap)
            Result.success(result.text)
        } catch (e: NotFoundException) {
            Result.failure(e)
        } catch (e: ChecksumException) {
            Result.failure(e)
        } catch (e: FormatException) {
            Result.failure(e)
        }
    }
}