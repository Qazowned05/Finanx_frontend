package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PaymentProvider

interface WebhooksApi {
    /**
     * POST webhooks/{provider}
     * Receive provider webhook
     * Receives an inbound webhook notification from a payment provider.
     * Responses:
     *  - 200: Accepted (duplicates are idempotent)
     *  - 401: Invalid signature
     *
     * @param provider Payment provider
     * @param body 
     * @param xSignature Signature header sent by the provider (optional)
     * @return [Unit]
     */
    @POST("webhooks/{provider}")
    suspend fun receiveWebhook(@Path("provider") provider: PaymentProvider, @Body body: kotlin.String, @Header("X-Signature") xSignature: kotlin.String? = null): Response<Unit>

}
