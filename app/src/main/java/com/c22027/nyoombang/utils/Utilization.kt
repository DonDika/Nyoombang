package com.c22027.nyoombang.utils


import android.annotation.SuppressLint

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*



/*
//Midtrans
const val MERCHANT_CLIENT_KEY = "SB-Mid-client-EBHBcEjtZQo548H2"
const val MERCHANT_BASE_CHECKOUT_URL = "https://dikatech.000webhostapp.com/midtrans.php/"
const val AUTH_TOKEN = "SB-Mid-server-hUBBoStu1BH4_poDN619HnaT"

//formatting
fun amountFormat(number: Double): String{
    val numberFormat: NumberFormat = DecimalFormat("####")
    return numberFormat.format(number)
}
*/


object Utilization {

    private const val FILENAME_FORMAT = "dd-MM-yyyy"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
    const val MERCHANT_CLIENT_KEY = "SB-Mid-client-lN6SPqw0e2uVB0LG"
    const val MERCHANT_BASE_CHECKOUT_URL = "https://dikatech.000webhostapp.com/midtrans.php/"
    const val TOKEN = "SB-Mid-server-gPE-TsEAWFydaQwXUNiKlSLF"


    const val PREF_USERNAME = "pref_username"
    const val PREF_ORDER_ID = "order_id"



    fun amountFormat(number: Double): String{
        val numberFormat: NumberFormat = DecimalFormat("####")
        return numberFormat.format(number)
    }
    
        //Implement here
//    fun formatDate(currentDateString: String, targetTimeZone: String): String {
//        val instant = Instant.parse(currentDateString)
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
//            .withZone(ZoneId.of(targetTimeZone))
//        return formatter.format(instant)
//    }
//
//    fun formatTime(currentDateString: String, targetTimeZone: String): String {
//        val instant = Instant.parse(currentDateString)
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//            .withZone(ZoneId.of(targetTimeZone))
//        return formatter.format(instant)
//    }


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







}



