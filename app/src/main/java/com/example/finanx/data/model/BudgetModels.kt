package com.example.finanx.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BudgetRequest(
    @Json(name = "description") val description: String,
    @Json(name = "limitAmount") val limitAmount: Double,
    @Json(name = "categoryId") val categoryId: Long,
    @Json(name = "userId") val userId: Long,
    @Json(name = "alertable") val alertable: Boolean,
    @Json(name = "percentAlert") val percentAlert: Int,
    @Json(name = "recurrenceRule") val recurrenceRule: RecurrenceRuleRequest
)

@JsonClass(generateAdapter = true)
data class RecurrenceRuleRequest(
    @Json(name = "recurrenceType") val recurrenceType: String,
    @Json(name = "interval") val interval: Int,
    @Json(name = "dayOfWeek") val dayOfWeek: String,
    @Json(name = "start") val start: String,
    @Json(name = "end") val end: String
)

@JsonClass(generateAdapter = true)
data class BudgetResponse(
    @Json(name = "id") val id: Long,
    @Json(name = "description") val description: String,
    @Json(name = "limitAmount") val limitAmount: Double,
    @Json(name = "state") val state: String,
    @Json(name = "categoryId") val categoryId: Long,
    @Json(name = "userId") val userId: Long
)
