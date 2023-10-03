package com.globa.balinasofttestapp.common.util

import android.content.Context
import android.net.Uri
import java.io.FileInputStream

fun readUri(context: Context, uri: Uri): ByteArray {
    val pfd = context.contentResolver.openFileDescriptor(uri, "r")!!
    assert(pfd.statSize <= Int.MAX_VALUE)
    val data = ByteArray(pfd.statSize.toInt())
    val fd = pfd.fileDescriptor
    val fileStream = FileInputStream(fd)
    fileStream.read(data)
    pfd.close()
    return data
}