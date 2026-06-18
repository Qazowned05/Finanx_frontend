package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.GoogleLoginRequest
import org.openapitools.client.models.LoginRequest
import org.openapitools.client.models.LoginResponse
import org.openapitools.client.models.UserRequest

interface AuthControllerApi {
    /**
     * POST auth/login
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param loginRequest 
     * @return [LoginResponse]
     */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    /**
     * POST auth/google
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param googleLoginRequest 
     * @return [LoginResponse]
     */
    @POST("auth/google")
    suspend fun loginWithGoogle(@Body googleLoginRequest: GoogleLoginRequest): Response<LoginResponse>

    /**
     * POST auth/register
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userRequest 
     * @return [LoginResponse]
     */
    @POST("auth/register")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<LoginResponse>

}
