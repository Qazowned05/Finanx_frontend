package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse

interface UsersApi {
    /**
     * POST users/{id}/block
     * Bloquear usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("users/{id}/block")
    suspend fun blockUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * DELETE users/{id}/delete
     * Eliminar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("users/{id}/delete")
    suspend fun deleteUser(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * POST users/{id}/disable
     * Deshabilitar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("users/{id}/disable")
    suspend fun disableUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * POST users/{id}/activate
     * Habilitar usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("users/{id}/activate")
    suspend fun enableUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * GET users
     * Listar todos los usuarios
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [kotlin.collections.List<UserResponse>]
     */
    @GET("users")
    suspend fun findAllUser(): Response<kotlin.collections.List<UserResponse>>

    /**
     * GET users/{id}
     * Buscar usuario por ID
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @GET("users/{id}")
    suspend fun findUserById(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * GET users/timezones
     * Listar zonas horarias disponibles
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [kotlin.collections.List<kotlin.String>]
     */
    @GET("users/timezones")
    suspend fun listTimeZones(): Response<kotlin.collections.List<kotlin.String>>

    /**
     * POST users/register
     * Registrar un nuevo usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userRequest 
     * @return [LoginResponse]
     */
    @POST("users/register")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<LoginResponse>

    /**
     * POST users/{id}/reset-data
     * Resetear datos de usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("users/{id}/reset-data")
    suspend fun resetUserData(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET users/search
     * Buscar usuarios por nombre
     * 
     * Responses:
     *  - 200: OK
     *
     * @param string 
     * @return [kotlin.collections.List<UserResponse>]
     */
    @GET("users/search")
    suspend fun searchUserByName(@Query("string") string: kotlin.String): Response<kotlin.collections.List<UserResponse>>

    /**
     * PUT users/{id}
     * Actualizar datos de usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param userRequest 
     * @return [UserResponse]
     */
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: kotlin.Long, @Body userRequest: UserRequest): Response<UserResponse>

}
