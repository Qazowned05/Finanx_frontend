package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.TransactionScheduleResponse

interface ScheduleTransactionsApi {

    /**
    * enum for parameter state
    */
    enum class StateFindAllTScheduleByUserAndState(val value: kotlin.String) {
        @Json(name = "RUNNING") RUNNING("RUNNING"),
        @Json(name = "PAUSED") PAUSED("PAUSED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "CREATED") CREATED("CREATED"),
        @Json(name = "ERROR") ERROR("ERROR")
    }

    /**
     * GET schedule/transactions/user/{idUser}/{state}
     * Listar programaciones de transacciones
     * Obtiene programaciones de transacciones por usuario y estado.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID usuario
     * @param state Estado
     * @return [kotlin.collections.List<TransactionScheduleResponse>]
     */
    @GET("schedule/transactions/user/{idUser}/{state}")
    suspend fun findAllTScheduleByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<TransactionScheduleResponse>>

}
