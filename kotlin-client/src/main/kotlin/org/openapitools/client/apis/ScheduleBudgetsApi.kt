package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BudgetScheduleResponse

interface ScheduleBudgetsApi {

    /**
    * enum for parameter state
    */
    enum class StateFindAllBudgetScheduleByUserAndState(val value: kotlin.String) {
        @Json(name = "RUNNING") RUNNING("RUNNING"),
        @Json(name = "PAUSED") PAUSED("PAUSED"),
        @Json(name = "FINALIZED") FINALIZED("FINALIZED"),
        @Json(name = "CANCELLED") CANCELLED("CANCELLED"),
        @Json(name = "CREATED") CREATED("CREATED"),
        @Json(name = "ERROR") ERROR("ERROR")
    }

    /**
     * GET schedule/budgets/user/{idUser}/{state}
     * Listar programaciones de presupuestos
     * Obtiene programaciones de presupuestos por usuario y estado.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID usuario
     * @param state Estado
     * @return [kotlin.collections.List<BudgetScheduleResponse>]
     */
    @GET("schedule/budgets/user/{idUser}/{state}")
    suspend fun findAllBudgetScheduleByUserAndState(@Path("idUser") idUser: kotlin.Long, @Path("state") state: kotlin.String): Response<kotlin.collections.List<BudgetScheduleResponse>>

}
