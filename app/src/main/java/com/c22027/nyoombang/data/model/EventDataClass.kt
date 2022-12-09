package com.c22027.nyoombang.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class EventDataClass(
    val eventId: String,
    val userId: String,
    val userName: String,
    val eventName: String,
    val eventPicture: String? = null,
    val eventDescription: String,
    val endOfDate: String,
    val totalAmount: Int? = 0
): Parcelable
