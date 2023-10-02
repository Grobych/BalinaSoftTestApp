package com.globa.balinasofttestapp.common.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    private val dateFormat = SimpleDateFormat("dd.mm.yyyy", Locale.ENGLISH)
    private val extendDateFormat = SimpleDateFormat("dd.mm.yyyy, HH:mm:ss", Locale.ENGLISH)

    fun getSimpleDate(date: Long) = dateFormat.format(date * 1000L)
    fun getExtendDate(date: Long) = extendDateFormat.format(date * 1000L)
}