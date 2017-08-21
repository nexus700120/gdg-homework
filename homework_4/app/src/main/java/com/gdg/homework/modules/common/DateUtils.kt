package com.gdg.homework.modules.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by vkirillov on 21.08.2017.
 */
@SuppressLint("SimpleDateFormat")
fun formatDate(timestamp: Long): String =
        SimpleDateFormat("dd.MM.yyyy").format(Date(timestamp * 1000))

@SuppressLint("SimpleDateFormat")
fun getNumberOfMonth(timestamp: Long): Float =
        SimpleDateFormat("dd").format(Date(timestamp * 1000)).toFloat()