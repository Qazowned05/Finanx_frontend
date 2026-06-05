package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.TransactionScheduleResponse

interface TransactionScheduleControllerApi {

    /**
    * enum for parameter state
    */
    enum class StateFindAllByUserAndState1(val value: kotlin.String) {
        @Json(name = "RUNNING") RUNNING("RUNNING"),
        @Json(name = "PAUSED") PAUSED("PAUSED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "CREATED") CREATED("CREATED"),
        @Json(name = "ERROR") ERROR("ERROR")
    }

    /**
     * GET schedule/transactions/{idUser}/{state}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param state 
     * @return [kotlin.collections.List<TransactionScheduleResponse>]
     */
    @GET("schedule/transactions/{idUser}/{state}")
    suspend fun findAllByUserAndState1(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<TransactionScheduleResponse>>

}
