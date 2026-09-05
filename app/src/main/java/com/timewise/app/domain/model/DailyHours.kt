package com.timewise.app.domain.model

import java.time.LocalDate

data class DailyHours(
    val date: LocalDate,
    val hours: Float
)