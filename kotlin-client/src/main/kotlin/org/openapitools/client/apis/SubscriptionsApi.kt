package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.SubscriptionRequest
import org.openapitools.client.models.SubscriptionResponse

interface SubscriptionsApi {
    /**
     * POST subscriptions/{id}/activate
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
    @POST("subscriptions/{id}/activate")
    suspend fun activateSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * POST subscriptions/{id}/cancel
     * Cancel subscription
     * Cancels a subscription and disables auto renew.
     * Responses:
     *  - 200: Canceled
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @POST("subscriptions/{id}/cancel")
    suspend fun cancelSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * POST subscriptions
     * Create subscription
     * Creates a new subscription in PENDING state, awaiting payment confirmation.
     * Responses:
     *  - 201: Created
     *  - 400: Invalid data
     *
     * @param subscriptionRequest 
     * @return [SubscriptionResponse]
     */
    @POST("subscriptions")
    suspend fun createSubscription(@Body subscriptionRequest: SubscriptionRequest): Response<SubscriptionResponse>

    /**
     * GET subscriptions/user/{userId}
     * List subscriptions by user
     * Gets all subscriptions belonging to a user.
     * Responses:
     *  - 200: List retrieved
     *  - 404: User not found
     *
     * @param userId User id
     * @return [kotlin.collections.List<SubscriptionResponse>]
     */
    @GET("subscriptions/user/{userId}")
    suspend fun findAllSubscriptionsByUser(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<SubscriptionResponse>>

    /**
     * GET subscriptions/{id}
     * Find subscription
     * Gets a subscription by id.
     * Responses:
     *  - 200: Found
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @GET("subscriptions/{id}")
    suspend fun findSubscriptionById(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

    /**
     * POST subscriptions/{id}/renew
     * Renew subscription
     * Opens the next billing period for an active subscription.
     * Responses:
     *  - 200: Renewed
     *  - 404: Not found
     *
     * @param id Subscription id
     * @return [SubscriptionResponse]
     */
    @POST("subscriptions/{id}/renew")
    suspend fun renewSubscription(@Path("id") id: kotlin.Long): Response<SubscriptionResponse>

}
