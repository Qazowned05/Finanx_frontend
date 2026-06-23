package com.example.finanx.ui.util

import org.openapitools.client.models.PeriodRequest
import java.time.YearMonth

fun buildCurrentMonthPeriod(userId: Long): PeriodRequest {
    val currentMonth = YearMonth.now()
    val start = currentMonth.atDay(1)
    val end = currentMonth.atEndOfMonth()
    return PeriodRequest(start, end, userId)
}