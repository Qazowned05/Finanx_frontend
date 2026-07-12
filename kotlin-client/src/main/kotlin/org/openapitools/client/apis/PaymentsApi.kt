package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PaymentRequest
import org.openapitools.client.models.PaymentResponse

interface PaymentsApi {
    /**
     * POST payments
     * Create payment
     * Starts a new payment attempt against the configured provider.
     * Responses:
     *  - 201: Created
     *  - 400: Invalid data
     *
     * @param paymentRequest 
     * @return [PaymentResponse]
     */
    @POST("payments")
    suspend fun createPayment(@Body paymentRequest: PaymentRequest): Response<PaymentResponse>

    /**
     * GET payments/subscription/{subscriptionId}
     * List payments by subscription
     * Gets all payments linked to a subscription.
     * Responses:
     *  - 200: List retrieved
     *
     * @param subscriptionId Subscription id
     * @return [kotlin.collections.List<PaymentResponse>]
     */
    @GET("payments/subscription/{subscriptionId}")
    suspend fun findAllPaymentsBySubscription(@Path("subscriptionId") subscriptionId: kotlin.Long): Response<kotlin.collections.List<PaymentResponse>>

    /**
     * GET payments/user/{userId}
     * List payments by user
     * Gets all payments made by a user.
     * Responses:
     *  - 200: List retrieved
     *  - 404: User not found
     *
     * @param userId User id
     * @return [kotlin.collections.List<PaymentResponse>]
     */
    @GET("payments/user/{userId}")
    suspend fun findAllPaymentsByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<PaymentResponse>>

    /**
     * GET payments/{id}
     * Find payment
     * Gets a payment by id.
     * Responses:
     *  - 200: Found
     *  - 404: Not found
     *
     * @param id Payment id
     * @return [PaymentResponse]
     */
    @GET("payments/{id}")
    suspend fun findPaymentById(@Path("id") id: kotlin.Long): Response<PaymentResponse>

    /**
     * POST payments/{id}/refund
     * Refund payment
     * Requests a refund on the provider and marks the payment as REFUNDED.
     * Responses:
     *  - 200: Refunded
     *  - 404: Not found
     *  - 409: Invalid state transition
     *
     * @param id Payment id
     * @return [PaymentResponse]
     */
    @POST("payments/{id}/refund")
    suspend fun refundPayment(@Path("id") id: kotlin.Long): Response<PaymentResponse>

}
