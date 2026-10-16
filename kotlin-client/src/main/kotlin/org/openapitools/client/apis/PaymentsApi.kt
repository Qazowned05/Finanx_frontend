package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponsePaymentResponse
import org.openapitools.client.models.PaymentRequest
import org.openapitools.client.models.PaymentResponse
import org.openapitools.client.models.PaymentState

interface PaymentsApi {
    /**
     * POST api/v1/payments
     * Create payment
     * Starts a new payment attempt against the configured provider.
     * Responses:
     *  - 201: Created
     *  - 400: Invalid data
     *
     * @param paymentRequest 
     * @return [PaymentResponse]
     */
    @POST("api/v1/payments")
    suspend fun createPayment(@Body paymentRequest: PaymentRequest): Response<PaymentResponse>

    /**
     * GET api/v1/payments/{id}
     * Find payment
     * Gets a payment by id.
     * Responses:
     *  - 200: Found
     *  - 404: Not found
     *
     * @param id Payment id
     * @return [PaymentResponse]
     */
    @GET("api/v1/payments/{id}")
    suspend fun findPaymentById(@Path("id") id: kotlin.Long): Response<PaymentResponse>

    /**
     * GET api/v1/payments
     * List payments by user
     * Lists payments of the authenticated user with optional state, subscriptionId and from/to filters, plus pagination and sort.
     * Responses:
     *  - 200: List retrieved
     *  - 404: User not found
     *
     * @param state  (optional)
     * @param subscriptionId  (optional)
     * @param from  (optional)
     * @param to  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponsePaymentResponse]
     */
    @GET("api/v1/payments")
    suspend fun listPayments(@Query("state") state: PaymentState? = null, @Query("subscriptionId") subscriptionId: kotlin.Long? = null, @Query("from") from: java.time.LocalDate? = null, @Query("to") to: java.time.LocalDate? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponsePaymentResponse>

    /**
     * POST api/v1/payments/{id}/refund
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
    @POST("api/v1/payments/{id}/refund")
    suspend fun refundPayment(@Path("id") id: kotlin.Long): Response<PaymentResponse>

}
