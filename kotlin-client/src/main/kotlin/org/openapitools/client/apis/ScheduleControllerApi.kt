package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.FindById2200Response
import org.openapitools.client.models.Update2Request

interface ScheduleControllerApi {
    /**
     * POST schedule
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param update2Request 
     * @return [FindById2200Response]
     */
    @POST("schedule")
    suspend fun create1(@Body update2Request: Update2Request): Response<FindById2200Response>

    /**
     * GET schedule/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [FindById2200Response]
     */
    @GET("schedule/{id}")
    suspend fun findById2(@Path("id") id: kotlin.Long): Response<FindById2200Response>

    /**
     * PUT schedule/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param update2Request 
     * @return [FindById2200Response]
     */
    @PUT("schedule/{id}")
    suspend fun update2(@Path("id") id: kotlin.Long, @Body update2Request: Update2Request): Response<FindById2200Response>

}
