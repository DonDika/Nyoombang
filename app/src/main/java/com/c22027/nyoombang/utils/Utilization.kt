package com.c22027.nyoombang.utils


import android.annotation.SuppressLint
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
import java.time.LocalDateTime
import java.util.*


object Utilization {

    private const val FILENAME_FORMAT = "dd-MM-yyyy"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    fun amountFormat(number: Double): String{
        val numberFormat: NumberFormat = DecimalFormat("####")
        return numberFormat.format(number)
    }

    fun amountDonationFormat(number: Int): String{
        val numberFormat: NumberFormat = DecimalFormat("#,###")
        return numberFormat.format(number)
    }
    

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
//
//    fun formatDateTrans(timestamp: String): String? {
//        val d: Date =
//        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(timestamp) as Date
//        val cal = Calendar.getInstance()
//        cal.time = d
//        return SimpleDateFormat("dd MMM yyyy").format(cal.time)
//    }
//fun formatTimeTrans(timestamp: String): String? {
//    val d: Date =
//    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(timestamp) as Date
//    val cal = Calendar.getInstance()
//    cal.time = d
//        return SimpleDateFormat("HH:mm").format(cal.time)
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



