package com.example.finanx.data.repository

import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.models.TransactionsDashboard
import retrofit2.Response

class AnalyticsRepository(
    private val api: AnalyticsControllerApi
) {
    suspend fun getBalanceDashboard(
        period: PeriodRequest,
        prompt: String? = null
    ): Response<TransactionsDashboard> =
        withContext(Dispatchers.IO) { api.balance(period) }
}
