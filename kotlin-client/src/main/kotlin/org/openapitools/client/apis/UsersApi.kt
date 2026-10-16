package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.PageResponseUserResponse
import org.openapitools.client.models.TimeZone
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse

interface UsersApi {
    /**
     * POST api/v1/users/{id}/block
     * Bloquear usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("api/v1/users/{id}/block")
    suspend fun blockUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * DELETE api/v1/users/{id}/delete
     * Eliminar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("api/v1/users/{id}/delete")
    suspend fun deleteUser(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST api/v1/users/{id}/disable
     * Deshabilitar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("api/v1/users/{id}/disable")
    suspend fun disableUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * POST api/v1/users/{id}/activate
     * Habilitar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("api/v1/users/{id}/activate")
    suspend fun enableUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * GET api/v1/users/me
     * Obtener el usuario autenticado
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [UserResponse]
     */
    @GET("api/v1/users/me")
    suspend fun findCurrentUser(): Response<UserResponse>

    /**
     * GET api/v1/users/{id}
     * Buscar usuario por ID
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @GET("api/v1/users/{id}")
    suspend fun findUserById(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * GET api/v1/users/timezones
     * Listar zonas horarias disponibles
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [kotlin.collections.List<TimeZone>]
     */
    @GET("api/v1/users/timezones")
    suspend fun listTimeZones(): Response<kotlin.collections.List<TimeZone>>

    /**
     * GET api/v1/users
     * Listar usuarios
     * Lista paginada con búsqueda opcional por nombre, apellido, email o username.
     * Responses:
     *  - 200: OK
     *
     * @param search  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("createdAt,DESC"))
     * @return [PageResponseUserResponse]
     */
    @GET("api/v1/users")
    suspend fun listUsers(@Query("search") search: kotlin.String? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("createdAt,DESC")): Response<PageResponseUserResponse>

    /**
     * POST api/v1/users/register
     * Registrar un nuevo usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userRequest 
     * @return [LoginResponse]
     */
    @POST("api/v1/users/register")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<LoginResponse>

    /**
     * POST api/v1/users/{id}/reset-data
     * Resetear datos de usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("api/v1/users/{id}/reset-data")
    suspend fun resetUserData(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * PUT api/v1/users/{id}
     * Actualizar datos de usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param userRequest 
     * @return [UserResponse]
     */
    @PUT("api/v1/users/{id}")
    suspend fun updateUser(@Path("id") id: kotlin.Long, @Body userRequest: UserRequest): Response<UserResponse>

}
