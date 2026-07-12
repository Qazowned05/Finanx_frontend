package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.FcmTokenResponse
import org.openapitools.client.models.RegisterFcmTokenRequest

interface FCMTokensApi {
    /**
     * GET users/{userId}/fcm-tokens
     * Listar tokens FCM del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.List<FcmTokenResponse>]
     */
    @GET("users/{userId}/fcm-tokens")
    suspend fun listFcmTokens(@Path("userId") userId: kotlin.Long): Response<kotlin.collections.List<FcmTokenResponse>>

    /**
     * POST users/{userId}/fcm-tokens
     * Registrar token FCM
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param registerFcmTokenRequest 
     * @return [FcmTokenResponse]
     */
    @POST("users/{userId}/fcm-tokens")
    suspend fun registerFcmToken(@Path("userId") userId: kotlin.Long, @Body registerFcmTokenRequest: RegisterFcmTokenRequest): Response<FcmTokenResponse>

    /**
     * DELETE users/{userId}/fcm-tokens/{tokenId}
     * Eliminar token FCM
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param tokenId 
     * @return [Unit]
     */
    @DELETE("users/{userId}/fcm-tokens/{tokenId}")
    suspend fun removeFcmToken(@Path("userId") userId: kotlin.Long, @Path("tokenId") tokenId: kotlin.Long): Response<Unit>

}
