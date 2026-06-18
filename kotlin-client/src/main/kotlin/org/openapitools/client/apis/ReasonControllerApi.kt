package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.ReasonRequest
import org.openapitools.client.models.ReasonResponse

interface ReasonControllerApi {
    /**
     * POST reasons
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param reasonRequest 
     * @return [ReasonResponse]
     */
    @POST("reasons")
    suspend fun createReason(@Body reasonRequest: ReasonRequest): Response<ReasonResponse>

    /**
     * POST reasons/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("reasons/{id}/disable")
    suspend fun disableReason(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET reasons/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [ReasonResponse]
     */
    @GET("reasons/{id}")
    suspend fun findReasonById(@Path("id") id: kotlin.Long): Response<ReasonResponse>

    /**
     * GET reasons/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<ReasonResponse>]
     */
    @GET("reasons/user/{idUser}")
    suspend fun findReasonsByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<ReasonResponse>>

    /**
     * PUT reasons/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param reasonRequest 
     * @return [ReasonResponse]
     */
    @PUT("reasons/{id}")
    suspend fun updateReason(@Path("id") id: kotlin.Long, @Body reasonRequest: ReasonRequest): Response<ReasonResponse>

}
