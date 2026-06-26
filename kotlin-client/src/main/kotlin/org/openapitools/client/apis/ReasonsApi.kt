package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.ReasonRequest
import org.openapitools.client.models.ReasonResponse

interface ReasonsApi {
    /**
     * POST reasons
     * Crear razon
     * Crea una nueva razon.
     * Responses:
     *  - 201: Creada
     *  - 400: Datos invalidos
     *
     * @param reasonRequest 
     * @return [ReasonResponse]
     */
    @POST("reasons")
    suspend fun createReason(@Body reasonRequest: ReasonRequest): Response<ReasonResponse>

    /**
     * POST reasons/{id}/disable
     * Deshabilitar razon
     * Deshabilita una razon.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la razon
     * @return [Unit]
     */
    @POST("reasons/{id}/disable")
    suspend fun disableReason(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET reasons/user/{idUser}
     * Listar razones por usuario
     * Obtiene todas las razones asociadas a un usuario.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param idUser ID del usuario
     * @return [kotlin.collections.List<ReasonResponse>]
     */
    @GET("reasons/user/{idUser}")
    suspend fun findAllReasonByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<ReasonResponse>>

    /**
     * GET reasons/{id}
     * Buscar razon
     * Obtiene una razon por ID.
     * Responses:
     *  - 200: Encontrada
     *  - 404: No encontrada
     *
     * @param id ID de la razon
     * @return [ReasonResponse]
     */
    @GET("reasons/{id}")
    suspend fun findReasonById(@Path("id") id: kotlin.Long): Response<ReasonResponse>

    /**
     * PUT reasons/{id}
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
    @PUT("reasons/{id}")
    suspend fun updateReason(@Path("id") id: kotlin.Long, @Body reasonRequest: ReasonRequest): Response<ReasonResponse>

}
