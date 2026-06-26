package com.example.finanx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionsDashboard
import retrofit2.Response

class AnalyticsRepository(
    private val api: AnalyticsApi
) {
    suspend fun getBalanceDashboard(
        period: PeriodRequest,
        prompt: String? = null
    ): Response<TransactionsDashboard> =
        withContext(Dispatchers.IO) { api.getBalanceDashboard(period) }
}
