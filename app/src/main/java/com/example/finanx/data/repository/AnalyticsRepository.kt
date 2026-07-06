package com.example.finanx.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.models.PeriodRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionsDashboard
import retrofit2.Response
import javax.inject.Inject

class AnalyticsRepository @Inject constructor(
    private val api: AnalyticsApi
) {
    suspend fun getBalanceDashboard(
        period: PeriodRequest
    ): Response<TransactionsDashboard> =
        withContext(Dispatchers.IO) { api.getBalanceDashboard(period) }
}