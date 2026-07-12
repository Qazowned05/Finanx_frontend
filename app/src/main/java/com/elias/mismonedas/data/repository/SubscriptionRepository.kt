package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.PaymentsApi
import org.openapitools.client.apis.SubscriptionsApi
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.SubscriptionRequest
import org.openapitools.client.models.SubscriptionResponse
import java.io.IOException
import javax.inject.Inject

class SubscriptionRepository @Inject constructor(
    private val subscriptionsApi: SubscriptionsApi,
    private val paymentsApi: PaymentsApi
) {
    suspend fun getCurrentSubscription(userId: Long): SubscriptionResponse? =
        withContext(Dispatchers.IO) {
            val response = subscriptionsApi.findAllSubscriptionsByUser(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.firstOrNull()
            } else {
                null
            }
        }

    suspend fun createSubscription(request: SubscriptionRequest): SubscriptionResponse =
        withContext(Dispatchers.IO) {
            val response = subscriptionsApi.createSubscription(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al crear suscripción: ${response.code()}")
            }
        }

    suspend fun cancelSubscription(subscriptionId: Long): SubscriptionResponse =
        withContext(Dispatchers.IO) {
            val response = subscriptionsApi.cancelSubscription(subscriptionId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al cancelar suscripción: ${response.code()}")
            }
        }

    suspend fun getPaymentHistory(userId: Long): List<PaymentResponse> =
        withContext(Dispatchers.IO) {
            val response = paymentsApi.findAllPaymentsByUser(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        }
}
