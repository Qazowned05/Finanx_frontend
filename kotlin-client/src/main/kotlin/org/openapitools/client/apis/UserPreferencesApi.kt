package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.UserPreferencesRequest
import org.openapitools.client.models.UserPreferencesResponse

interface UserPreferencesApi {
    /**
     * POST api/v1/users/me/preferences
     * Crear preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userPreferencesRequest 
     * @return [UserPreferencesResponse]
     */
    @POST("api/v1/users/me/preferences")
    suspend fun createUserPreferences(@Body userPreferencesRequest: UserPreferencesRequest): Response<UserPreferencesResponse>

    /**
     * GET api/v1/users/me/preferences
     * Obtener preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @return [UserPreferencesResponse]
     */
    @GET("api/v1/users/me/preferences")
    suspend fun getUserPreferences(): Response<UserPreferencesResponse>

    /**
     * PUT api/v1/users/me/preferences
     * Actualizar preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userPreferencesRequest 
     * @return [UserPreferencesResponse]
     */
    @PUT("api/v1/users/me/preferences")
    suspend fun updateUserPreferences(@Body userPreferencesRequest: UserPreferencesRequest): Response<UserPreferencesResponse>

}
