package com.timewise.app.domain.model

import com.google.android.gms.common.util.Hex

data class TimeBlock (
    val id: Long = 0,
    val taskId: Long? = null,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val colorHex: String = "#6200EE",
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)