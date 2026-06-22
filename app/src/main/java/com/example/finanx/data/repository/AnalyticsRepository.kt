package com.example.finanx.data.repository

import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AnalyticsRepository(
    private val api: AnalyticsControllerApi
) {
    suspend fun getDashboard(
        period: PeriodRequest,
        prompt: String? = null
    ): Response<Budgets200Response> =
        withContext(Dispatchers.IO) { api.balance(period) }
}
