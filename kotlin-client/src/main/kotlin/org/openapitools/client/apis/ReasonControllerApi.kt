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
    suspend fun create3(@Body reasonRequest: ReasonRequest): Response<ReasonResponse>

    /**
     * POST reasons/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("reasons/{id}")
    suspend fun disable2(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET reasons/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<ReasonResponse>]
     */
    @GET("reasons/{idUser}")
    suspend fun findAll1(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<ReasonResponse>>

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
    suspend fun findById4(@Path("id") id: kotlin.Long): Response<ReasonResponse>

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
    suspend fun update4(@Path("id") id: kotlin.Long, @Body reasonRequest: ReasonRequest): Response<ReasonResponse>

}
