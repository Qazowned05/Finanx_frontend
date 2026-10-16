package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.PageResponseReasonResponse
import org.openapitools.client.models.ReasonRequest
import org.openapitools.client.models.ReasonResponse

interface ReasonsApi {
    /**
     * POST api/v1/reasons
     * Crear razon
     * Crea una nueva razon.
     * Responses:
     *  - 201: Creada
     *  - 400: Datos invalidos
     *
     * @param reasonRequest 
     * @return [ReasonResponse]
     */
    @POST("api/v1/reasons")
    suspend fun createReason(@Body reasonRequest: ReasonRequest): Response<ReasonResponse>

    /**
     * POST api/v1/reasons/{id}/disable
     * Deshabilitar razon
     * Deshabilita una razon.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la razon
     * @return [Unit]
     */
    @POST("api/v1/reasons/{id}/disable")
    suspend fun disableReason(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/reasons/{id}
     * Buscar razon
     * Obtiene una razon por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID de la razon
     * @return [ReasonResponse]
     */
    @GET("api/v1/reasons/{id}")
    suspend fun findReasonById(@Path("id") id: kotlin.Long): Response<ReasonResponse>

    /**
     * GET api/v1/reasons
     * Listar razones del usuario
     * Lista paginada de razones del usuario autenticado con filtros active y search; active&#x3D;true por defecto.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param active  (optional, default to true)
     * @param search  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("description,ASC"))
     * @return [PageResponseReasonResponse]
     */
    @GET("api/v1/reasons")
    suspend fun listReasons(@Query("active") active: kotlin.Boolean? = true, @Query("search") search: kotlin.String? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("description,ASC")): Response<PageResponseReasonResponse>

    /**
     * PUT api/v1/reasons/{id}
     * Actualizar razon
     * Actualiza una razon existente.
     * Responses:
     *  - 200: Actualizada
     *  - 400: Datos invalidos
     *  - 404: No encontrada
     *
     * @param id ID de la razon
     * @param reasonRequest 
     * @return [ReasonResponse]
     */
    @PUT("api/v1/reasons/{id}")
    suspend fun updateReason(@Path("id") id: kotlin.Long, @Body reasonRequest: ReasonRequest): Response<ReasonResponse>

}
