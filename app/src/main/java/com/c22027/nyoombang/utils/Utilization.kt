package com.c22027.nyoombang.utils

import java.text.DecimalFormat
import java.text.NumberFormat

object Utilization {

    //Midtrans
    const val MERCHANT_CLIENT_KEY = "SB-Mid-client-EBHBcEjtZQo548H2"
    const val MERCHANT_BASE_CHECKOUT_URL = "https://dikatech.000webhostapp.com/midtrans.php/"
    const val AUTH_TOKEN = "SB-Mid-server-hUBBoStu1BH4_poDN619HnaT"

    //formatting
    fun amountFormat(number: Double): String{
        val numberFormat: NumberFormat = DecimalFormat("####")
        return numberFormat.format(number)
    }

}

