package com.example.finanx.data.repository

import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.models.AnalyticsDashboard
import retrofit2.Response
import java.time.OffsetDateTime

class AnalyticsRepository(
    private val api: AnalyticsControllerApi
) {
    suspend fun getDashboard(
        userId: Long,
        start: OffsetDateTime,
        end: OffsetDateTime
    ): Response<AnalyticsDashboard> {
        return api.balance(userId, start, end)
    }
}