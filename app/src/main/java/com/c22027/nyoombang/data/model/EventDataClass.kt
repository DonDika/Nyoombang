package com.c22027.nyoombang.data.model

import com.google.firebase.database.DataSnapshot

data class EventDataClass(
    val event_id: String? = null,
    val user_id: String? = null,
    val eventName: String? = null,
    val eventPicture: String? = null,
    val eventDescription: String? = null,
    val endOfDate: String? = null,
    val totalAmount: String? = null
)
