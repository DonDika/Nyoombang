package com.c22027.nyoombang.data.model

data class EventResponse (
    var items: List<EventDataClass>? =null,
    var exception: Exception? = null
)