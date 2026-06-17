package com.example.finanx.data.repository

import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.models.Budgets200Response
import org.openapitools.client.models.PeriodRequest
import retrofit2.Response

class AnalyticsRepository(
    private val api: AnalyticsControllerApi
) {
    suspend fun getDashboard(
        period: PeriodRequest
    ): Response<Budgets200Response> {
        return api.balance(period)
    }
}