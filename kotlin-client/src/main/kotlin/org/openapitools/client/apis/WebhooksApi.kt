package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json


interface WebhooksApi {

    /**
    * enum for parameter provider
    */
    enum class ProviderReceiveWebhook(val value: kotlin.String) {
        @Json(name = "MERCADO_PAGO") MERCADO_PAGO("MERCADO_PAGO"),
        @Json(name = "CULQI") CULQI("CULQI"),
        @Json(name = "STRIPE") STRIPE("STRIPE"),
        @Json(name = "MANUAL_TRANSFER") MANUAL_TRANSFER("MANUAL_TRANSFER")
    }

    /**
     * POST webhooks/{provider}
     * Receive provider webhook
     * Receives an inbound webhook notification from a payment provider.
     * Responses:
     *  - 200: Accepted
     *  - 401: Invalid signature
     *  - 409: Duplicate event
     *
     * @param provider Payment provider
     * @param body 
     * @param xSignature Signature header sent by the provider (optional)
     * @return [Unit]
     */
    @POST("webhooks/{provider}")
    suspend fun receiveWebhook(@Path("provider") provider: kotlin.String, @Body body: kotlin.String, @Header("X-Signature") xSignature: kotlin.String? = null): Response<Unit>

}
