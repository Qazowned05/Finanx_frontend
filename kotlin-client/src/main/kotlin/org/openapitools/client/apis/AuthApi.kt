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

interface AuthApi {
    /**
     * POST api/v1/auth/register
     * Registro
     * Registro de nuevo usuario.
     * Responses:
     *  - 200: Registro exitoso
     *  - 400: Datos invalidos
     *
     * @param userRequest 
     * @return [LoginResponse]
     */
    @POST("api/v1/auth/register")
    suspend fun authRegister(@Body userRequest: UserRequest): Response<LoginResponse>

    /**
     * POST api/v1/auth/google
     * Login con Google
     * Autenticacion mediante Google.
     * Responses:
     *  - 200: Login exitoso
     *  - 400: Token invalido
     *
     * @param googleLoginRequest 
     * @return [LoginResponse]
     */
    @POST("api/v1/auth/google")
    suspend fun authWithGoogle(@Body googleLoginRequest: GoogleLoginRequest): Response<LoginResponse>

    /**
     * POST api/v1/auth/login
     * Login
     * Autenticacion de usuario.
     * Responses:
     *  - 200: Login exitoso
     *  - 400: Credenciales invalidas
     *
     * @param loginRequest 
     * @return [LoginResponse]
     */
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

}
