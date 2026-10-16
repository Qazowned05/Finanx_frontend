package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.PaymentsApi
import org.openapitools.client.apis.SubscriptionsApi
import org.openapitools.client.models.PageResponsePaymentResponse
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.PaymentState
import org.openapitools.client.models.SubscriptionRequest
import org.openapitools.client.models.SubscriptionResponse
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class SubscriptionRepository @Inject constructor(
    private val subscriptionsApi: SubscriptionsApi,
    private val paymentsApi: PaymentsApi
) {
    /**
     * Devuelve la suscripción actual del usuario usando el endpoint dedicado
     * `GET api/v1/subscriptions/current`. El backend responde 404 cuando el
     * usuario no tiene suscripción activa, así que ese caso se mapea a `null`
     * (no es un error).
     */
    suspend fun getCurrentSubscription(): SubscriptionResponse? =
        withContext(Dispatchers.IO) {
            val response = subscriptionsApi.getCurrentSubscription()
            when {
                response.isSuccessful -> response.body()
                response.code() == 404 -> null
                else -> throw IOException("Error al obtener plan: ${response.code()}")
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

    suspend fun getPaymentHistory(): PageResponsePaymentResponse? =
        withContext(Dispatchers.IO) {
            val response = paymentsApi.listPayments()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al obtener historial de pagos: ${response.code()}")
            }
        }

    suspend fun listPaymentPage(
        state: PaymentState? = null,
        subscriptionId: Long? = null,
        from: LocalDate? = null,
        to: LocalDate? = null,
        page: Int = 0,
        size: Int = DEFAULT_PAGE_SIZE
    ): PageResponsePaymentResponse =
        withContext(Dispatchers.IO) {
            val response = paymentsApi.listPayments(
                state = state,
                subscriptionId = subscriptionId,
                from = from,
                to = to,
                page = page,
                size = size
            )
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw IOException("Error al obtener historial de pagos: ${response.code()}")
            }
        }
}
