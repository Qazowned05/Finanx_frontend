package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.FcmTokenResponse
import org.openapitools.client.models.PageResponseFcmTokenResponse
import org.openapitools.client.models.RegisterFcmTokenRequest

interface FCMTokensApi {
    /**
     * GET api/v1/fcm-tokens
     * Listar tokens FCM del usuario
     * Lista paginada con page, size y sort estándar.
     * Responses:
     *  - 200: OK
     *
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseFcmTokenResponse]
     */
    @GET("api/v1/fcm-tokens")
    suspend fun listFcmTokens(@Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseFcmTokenResponse>

    /**
     * POST api/v1/fcm-tokens
     * Registrar token FCM
     * 
     * Responses:
     *  - 200: OK
     *
     * @param registerFcmTokenRequest 
     * @return [FcmTokenResponse]
     */
    @POST("api/v1/fcm-tokens")
    suspend fun registerFcmToken(@Body registerFcmTokenRequest: RegisterFcmTokenRequest): Response<FcmTokenResponse>

    /**
     * DELETE api/v1/fcm-tokens/{tokenId}
     * Eliminar token FCM
     * 
     * Responses:
     *  - 200: OK
     *
     * @param tokenId 
     * @return [Unit]
     */
    @DELETE("api/v1/fcm-tokens/{tokenId}")
    suspend fun removeFcmToken(@Path("tokenId") tokenId: kotlin.Long): Response<Unit>

}
