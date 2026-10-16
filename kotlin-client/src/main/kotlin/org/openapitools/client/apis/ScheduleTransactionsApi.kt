package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseTransactionScheduleResponse
import org.openapitools.client.models.ScheduleState

interface ScheduleTransactionsApi {
    /**
     * GET api/v1/schedule/transactions
     * Listar programaciones de transacciones
     * Lista programaciones de transacciones activas y paginadas con filtro opcional state.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param state  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("nextRunAt,ASC"))
     * @return [PageResponseTransactionScheduleResponse]
     */
    @GET("api/v1/schedule/transactions")
    suspend fun listTransactionSchedules(@Query("state") state: ScheduleState? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("nextRunAt,ASC")): Response<PageResponseTransactionScheduleResponse>

}
