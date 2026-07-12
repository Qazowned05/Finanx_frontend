package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.models.BudgetsDashboard
import org.openapitools.client.models.PeriodRequest
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

    suspend fun getBudgetsDashboard(
        period: PeriodRequest
    ): Response<BudgetsDashboard> =
        withContext(Dispatchers.IO) { api.getBudgetsDashboard(period) }
}