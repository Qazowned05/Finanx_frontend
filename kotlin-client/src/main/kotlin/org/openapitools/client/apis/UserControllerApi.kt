package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest
import org.openapitools.client.models.UserResponse

interface UserControllerApi {
    /**
     * POST users/{id}/activate
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [UserResponse]
     */
    @POST("users/{id}/activate")
    suspend fun activateUser(@Path("id") id: kotlin.Long): Response<UserResponse>

    /**
     * POST users/{id}/block
     * 
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
     * POST users/{id}/disable
     * 
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
     * GET users
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [kotlin.collections.List<UserResponse>]
     */
    @GET("users")
    suspend fun findAllUsers(): Response<kotlin.collections.List<UserResponse>>

    /**
     * GET users/{id}
     * 
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
     * 
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
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userRequest 
     * @return [LoginResponse]
     */
    @POST("users/register")
    suspend fun registerUserAccount(@Body userRequest: UserRequest): Response<LoginResponse>

    /**
     * GET users/search
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param string 
     * @return [kotlin.collections.List<UserResponse>]
     */
    @GET("users/search")
    suspend fun searchUsersByName(@Query("string") string: kotlin.String): Response<kotlin.collections.List<UserResponse>>

    /**
     * PUT users/{id}
     * 
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
