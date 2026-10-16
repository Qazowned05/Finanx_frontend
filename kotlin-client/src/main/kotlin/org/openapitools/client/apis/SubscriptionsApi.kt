package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseSubscriptionResponse
import org.openapitools.client.models.SubscriptionRequest
import org.openapitools.client.models.SubscriptionResponse
import org.openapitools.client.models.SubscriptionState

interface SubscriptionsApi {
    /**
     * POST api/v1/subscriptions/{id}/activate
     * Activate subscription
     * Activates a PENDING or PAST_DUE subscription and opens a new billing period.
     * Responses:
     *  - 200: Activated
     *  - 404: Not found
     *  - 409: Invalid state transition
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @POST("api/v1/subscriptions/{id}/activate")
    suspend fun activateSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * POST api/v1/subscriptions/{id}/cancel
     * Cancel subscription
     * Cancels a subscription and disables auto renew.
     * Responses:
     *  - 200: Canceled
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @POST("api/v1/subscriptions/{id}/cancel")
    suspend fun cancelSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * POST api/v1/subscriptions
     * Create subscription
     * Creates a new subscription in PENDING state, awaiting payment confirmation.
     * Responses:
     *  - 201: Created
     *  - 400: Invalid data
     *
     * @param subscriptionRequest 
     * @return [SubscriptionResponse]
     */
    @POST("api/v1/subscriptions")
    suspend fun createSubscription(@Body subscriptionRequest: SubscriptionRequest): Response<SubscriptionResponse>

    /**
     * GET api/v1/subscriptions/{id}
     * Find subscription
     * Gets a subscription by id.
     * Responses:
     *  - 200: Found
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @GET("api/v1/subscriptions/{id}")
    suspend fun findSubscriptionById(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * GET api/v1/subscriptions/current
     * Get current subscription
     * Returns the active subscription of the authenticated user.
     * Responses:
     *  - 200: Active subscription found
     *  - 404: No active subscription
     *
     * @return [SubscriptionResponse]
     */
    @GET("api/v1/subscriptions/current")
    suspend fun getCurrentSubscription(): Response<SubscriptionResponse>

    /**
     * GET api/v1/subscriptions
     * List subscriptions by user
     * Lists subscriptions of the authenticated user with optional state and pagination/sort parameters.
     * Responses:
     *  - 200: List retrieved
     *  - 404: User not found
     *
     * @param state  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseSubscriptionResponse]
     */
    @GET("api/v1/subscriptions")
    suspend fun listSubscriptions(@Query("state") state: SubscriptionState? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseSubscriptionResponse>

    /**
     * POST api/v1/subscriptions/{id}/renew
     * Renew subscription
     * Opens the next billing period for an active subscription.
     * Responses:
     *  - 200: Renewed
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @POST("api/v1/subscriptions/{id}/renew")
    suspend fun renewSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

}
