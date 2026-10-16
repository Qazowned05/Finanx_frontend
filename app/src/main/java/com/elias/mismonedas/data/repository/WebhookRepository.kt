package com.elias.mismonedas.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.WebhooksApi
import org.openapitools.client.models.PaymentProvider
import java.io.IOException
import javax.inject.Inject

class WebhookRepository @Inject constructor(
    private val api: WebhooksApi
) {
    suspend fun testWebhook(provider: PaymentProvider, body: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.receiveWebhook(provider, body)
                if (response.isSuccessful) Result.success(Unit)
                else Result.failure(IOException("Error code: ${response.code()}"))
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }
}
