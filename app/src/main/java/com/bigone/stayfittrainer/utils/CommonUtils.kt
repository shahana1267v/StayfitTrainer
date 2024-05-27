package com.bigOne.StayFitTrainer.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.text.DecimalFormat

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.log10
import kotlin.math.pow

object CommonUtils {
    fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun getReadableDate(dt: LocalDate?) : String {
        //fri,01-01-2023
        val fm = DateTimeFormatter.ofPattern("EEE, dd-MM-yyyy")
        return dt?.format(fm)?:""
    }
    fun getReadableDate2(dt: OffsetDateTime) : String {
        //OP : 12 December 2023
        //without know format
        val fm = DateTimeFormatter.ofPattern("dd-mm-yyyy")
        return dt?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))?:""
    }



      fun getPathFromUri(uri: Uri, context: Context): String? {
        var res:String?=null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        if(cursor?.moveToFirst() == true){
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            res = cursor.getString(columnIndex)
        }
        cursor?.close()
        return  res
    }


    }
