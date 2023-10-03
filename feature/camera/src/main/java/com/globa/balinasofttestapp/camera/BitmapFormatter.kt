package com.globa.balinasofttestapp.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object BitmapFormatter {
    private const val maxSize = 1280
    private const val miB = 1024*1024

    fun createBitmap(byteArray: ByteArray): Bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    private fun resizeBitmap(image: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
        if (maxHeight > 0 && maxWidth > 0) {

            val sourceWidth: Int = image.width
            val sourceHeight: Int = image.height

            var targetWidth = maxWidth
            var targetHeight = maxHeight

            val sourceRatio = sourceWidth.toFloat() / sourceHeight.toFloat()
            val targetRatio = maxWidth.toFloat() / maxHeight.toFloat()

            if (targetRatio > sourceRatio) {
                targetWidth = (maxHeight.toFloat() * sourceRatio).toInt()
            } else {
                targetHeight = (maxWidth.toFloat() / sourceRatio).toInt()
            }

            return Bitmap.createScaledBitmap(
                image, targetWidth, targetHeight, true
            )

        } else {
            throw RuntimeException()
        }
    }

    private fun compress(bitmap: Bitmap, quality: Int): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            quality,
            outputStream
        )
        return outputStream
    }

    fun get64Image(byteArray: ByteArray): String {
        val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        val scaledBitmap = resizeBitmap(bitmap, maxSize,maxSize)
        var quality = 100
        var stream: ByteArrayOutputStream
        do {
            stream = compress(scaledBitmap, quality)
            quality -= 5
        } while (stream.size() > 2 * miB)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }

}