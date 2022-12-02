package com.c22027.nyoombang.helper

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.c22027.nyoombang.R
import java.io.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils  {

    private const val FILENAME_FORMAT = "dd-MM-yyyy"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    @SuppressLint("SuspiciousIndentation")
    fun uriToFile(selectedImg: Uri, context: Context): File{
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0)
            outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }


    fun formatDate(currentDate: String): String? {
        val currentFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val targetFormat = "dd MMM - HH:mm"
        val timezone = "GMT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timezone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException){
            ex.printStackTrace()
        }
        return targetDate
    }

}