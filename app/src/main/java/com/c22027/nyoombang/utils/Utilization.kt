package com.c22027.nyoombang.utils

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


//Midtrans
const val MERCHANT_CLIENT_KEY = "SB-Mid-client-EBHBcEjtZQo548H2"
const val MERCHANT_BASE_CHECKOUT_URL = "https://dikatech.000webhostapp.com/midtrans.php/"
const val AUTH_TOKEN = "SB-Mid-server-hUBBoStu1BH4_poDN619HnaT"

//formatting
fun amountFormat(number: Double): String{
    val numberFormat: NumberFormat = DecimalFormat("####")
    return numberFormat.format(number)
}


private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}
